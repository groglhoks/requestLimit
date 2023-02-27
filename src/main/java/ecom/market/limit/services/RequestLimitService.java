package ecom.market.limit.services;

import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import ecom.market.limit.exceptions.ToManyRequests;

@Service
public class RequestLimitService {

    @Value("${ecom.market.limit.requestCount:2}")
    private int requestCountLimit;

    @Value("${ecom.market.limit.minuteInterval:1}")
    private int minuteInterval;

    private ConcurrentHashMap<String, NavigableSet<LocalDateTime>> requestLimit = new ConcurrentHashMap<String, NavigableSet<LocalDateTime>>();


    /**
     * Method check request count for ip in time interval. Works with concurrency.
     * If request count is bigger than limit count then throws ToManyRequests RuntimeException.
     * @param ip
     */
    public void requestAllowed(String ip) {
        requestLimit.putIfAbsent(ip, new TreeSet<>());
        checkRequestCount(requestLimit.get(ip));
    }
    
    private void checkRequestCount(NavigableSet<LocalDateTime> rqsts) {

        synchronized(rqsts) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime before = now.minusMinutes(minuteInterval);
            SortedSet<LocalDateTime> lastRequests = rqsts.tailSet(before);
            if ((lastRequests.size() + 1) > requestCountLimit) {
                throw new ToManyRequests();      
            } else {
                rqsts.add(now);
            }
        }
    }
  
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void cleanSets() {
        requestLimit.forEachValue(1, reqSet -> {
            synchronized(reqSet) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime past = now.minusMinutes(minuteInterval);
                reqSet.removeIf(dateTime -> dateTime.isBefore(past));       
            }
        });
    }
}
