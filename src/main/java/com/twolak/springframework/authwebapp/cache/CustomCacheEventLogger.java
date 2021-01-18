package com.twolak.springframework.authwebapp.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

/**
 *
 * @author twolak
 */
@Slf4j
public class CustomCacheEventLogger implements CacheEventListener<Object, Object> {

	@Override
	public void onEvent(CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        log.info(String.format("Cache event = %1$s, Key = %2$s,  Old value = %3$s, New value = %4$s", 
        		cacheEvent.getType(), cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue()));
	}
}
