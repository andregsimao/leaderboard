package com.example.leaderboard.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.FilterConfig
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.concurrent.TimeUnit

@Component
class RequestThrottleFilter() : Filter {
    private val requestCountsPerIpAddress: LoadingCache<String, Int> =
        Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.SECONDS).build { 0 }

    @Value("\${leaderboard.max.requests.per.second}")
    private val maxRequestsPerSecond: Long = 5

    @Value("\${leaderboard.mapping.api.signature}")
    private val leaderBoardApiSignature: String = ""

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {}

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(servletRequest: ServletRequest, servletResponse: ServletResponse, filterChain: FilterChain) {
        val httpServletResponse = servletResponse as HttpServletResponse
        val clientIpAddress = getClientIP(servletRequest as HttpServletRequest)
        val requestURI = servletRequest.requestURI
        if (isMaximumRequestsPerSecondExceeded(requestURI, clientIpAddress)) {
            log.error(
                "[RequestThrottleFilter:doFilter] Too many requests for user $clientIpAddress. URI: $requestURI",
            )
            httpServletResponse.status = HttpStatus.TOO_MANY_REQUESTS.value()
            httpServletResponse.writer.write("Too many requests")
            return
        }
        filterChain.doFilter(servletRequest, servletResponse)
    }

    private fun isMaximumRequestsPerSecondExceeded(
        requestURI: String?,
        clientIpAddress: String,
    ): Boolean {
        if (requestURI == null || !requestURI.contains(leaderBoardApiSignature)) {
            return false
        }
        var requests = requestCountsPerIpAddress[clientIpAddress]
        if (requests == null) {
            requests = 0
        } else if (requests > maxRequestsPerSecond) {
            requestCountsPerIpAddress.asMap().remove(clientIpAddress)
            requestCountsPerIpAddress.put(clientIpAddress, requests)
            return true
        }
        requests++
        requestCountsPerIpAddress.put(clientIpAddress, requests)
        return false
    }

    private fun getClientIP(request: HttpServletRequest): String {
        val xfHeader = request.getHeader("X-Forwarded-For") ?: return request.remoteAddr
        return xfHeader.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
    }

    override fun destroy() {}
}
