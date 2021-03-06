/**
 *  Copyright 2011 Terracotta, Inc.
 *  Copyright 2011 Oracle America Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.jsr107.ri;

import java.util.ArrayList;

import javax.cache.CacheConfiguration;
import javax.cache.CacheEntryExpiryPolicy;
import javax.cache.CacheLoader;
import javax.cache.CacheWriter;
import javax.cache.event.CacheEntryListenerRegistration;
import javax.cache.transaction.IsolationLevel;
import javax.cache.transaction.Mode;

/**
 * The reference implementation of a {@link CacheConfiguration}.
 * 
 * @param <K> the type of keys maintained the cache
 * @param <V> the type of cached values
 * 
 * @author Brian Oliver
 * @since 1.0
 */
public class RICacheConfiguration<K, V> implements CacheConfiguration<K, V> {

    /**
     * The {@link CacheEntryListenerRegistration}s for the {@link CacheConfiguration}.
     */
    protected ArrayList<CacheEntryListenerRegistration<? super K, ? super V>> cacheEntryListenerRegistrations;

    /**
     * The {@link CacheLoader} for the built {@link CacheConfiguration}.
     */
    protected CacheLoader<K, ? extends V> cacheLoader;
    
    /**
     * The {@link CacheWriter} for the built {@link CacheConfiguration}.
     */
    protected CacheWriter<? super K, ? super V> cacheWriter;
    
    /**
     * The {@link CacheEntryExpiryPolicy} for the {@link CacheConfiguration}.
     */
    protected CacheEntryExpiryPolicy<? super K, ? super V> cacheEntryExpiryPolicy;
    
    /**
     * A flag indicating if "read-through" mode is required.
     */
    protected boolean isReadThrough;
    
    /**
     * A flag indicating if "write-through" mode is required.
     */
    protected boolean isWriteThrough;
    
    /**
     * A flag indicating if statistics gathering is enabled.
     */
    protected boolean isStatisticsEnabled;

    /**
     * A flag indicating if the cache will be store-by-value or store-by-reference.
     */
    protected boolean isStoreByValue;
    
    /**
     * A flag indicating if the cache will use transactions.
     */
    protected boolean isTransactionsEnabled;
    
    /**
     * The transaction {@link IsolationLevel}.
     */
    protected IsolationLevel txnIsolationLevel;

    /**
     * The transaction {@link Mode}.
     */
    protected Mode txnMode;
    
    /**
     * Constructs an {@link RICacheConfiguration} with the standard default values.
     */
    public RICacheConfiguration() {
        this.cacheEntryListenerRegistrations = new ArrayList<CacheEntryListenerRegistration<? super K, ? super V>>();
        this.cacheLoader = null;
        this.cacheWriter = null;
        this.cacheEntryExpiryPolicy = new CacheEntryExpiryPolicy.Default<K, V>();
        this.isReadThrough = false;
        this.isWriteThrough = false;
        this.isStatisticsEnabled = false;
        this.isStoreByValue = true;
        this.isTransactionsEnabled = false;
        this.txnIsolationLevel = IsolationLevel.NONE;
        this.txnMode = Mode.NONE;
    }
    
    /**
     * Constructs a {@link RICacheConfiguration} based on a set of parameters.
     * 
     * @param cacheEntryListenerRegistrations the {@link CacheEntryListenerRegistration}s
     * @param cacheLoader                     the {@link CacheLoader}
     * @param cacheWriter                     the {@link CacheWriter}
     * @param cacheEntryExpiryPolicy          the {@link CacheEntryExpiryPolicy}
     * @param isReadThrough                   is read-through caching supported
     * @param isWriteThrough                  is write-through caching supported
     * @param isStatisticsEnabled             are statistics enabled
     * @param isStoreByValue                  <code>true</code> if the "store-by-value" more
     *                                        or <code>false</code> for "store-by-reference"
     * @param isTransactionsEnabled           <code>true</code> if transactions are enabled                                       
     * @param txnIsolationLevel               the {@link IsolationLevel}
     * @param txnMode                         the {@link Mode}
     */
    public RICacheConfiguration(
            Iterable<CacheEntryListenerRegistration<? super K, ? super V>> cacheEntryListenerRegistrations,
            CacheLoader<K, ? extends V> cacheLoader,
            CacheWriter<? super K, ? super V> cacheWriter,
            CacheEntryExpiryPolicy<? super K, ? super V> cacheEntryExpiryPolicy, 
            boolean isReadThrough, 
            boolean isWriteThrough,
            boolean isStatisticsEnabled, 
            boolean isStoreByValue,
            boolean isTransactionsEnabled,
            IsolationLevel txnIsolationLevel, 
            Mode txnMode) {
        
        this.cacheEntryListenerRegistrations = new ArrayList<CacheEntryListenerRegistration<? super K, ? super V>>();
        for (CacheEntryListenerRegistration<? super K, ? super V> r : cacheEntryListenerRegistrations) {
            RICacheEntryListenerRegistration<? super K, ? super V> registration = 
                new RICacheEntryListenerRegistration<K, V>(r.getCacheEntryListener(), 
                                                           r.getCacheEntryFilter(), 
                                                           r.isOldValueRequired(), 
                                                           r.isSynchronous());
            this.cacheEntryListenerRegistrations.add(registration);
        }
        
        this.cacheLoader = cacheLoader;
        this.cacheWriter = cacheWriter;
        
        this.cacheEntryExpiryPolicy = cacheEntryExpiryPolicy;
        
        this.isReadThrough = isReadThrough;
        this.isWriteThrough = isWriteThrough;
        
        this.isStatisticsEnabled = isStatisticsEnabled;
        
        this.isStoreByValue = isStoreByValue;
        
        this.isTransactionsEnabled = isTransactionsEnabled;
        this.txnIsolationLevel = txnIsolationLevel;
        this.txnMode = txnMode;
    }
    
    /**
     * A copy-constructor for a {@link RICacheConfiguration}.
     * 
     * @param configuration  the {@link CacheConfiguration} from which to copy
     */
    public RICacheConfiguration(CacheConfiguration<K, V> configuration) {
        this(configuration.getCacheEntryListenerRegistrations(), 
             configuration.getCacheLoader(), 
             configuration.getCacheWriter(), 
             configuration.getCacheEntryExpiryPolicy(),
             configuration.isReadThrough(), 
             configuration.isWriteThrough(),
             configuration.isStatisticsEnabled(), 
             configuration.isStoreByValue(),
             configuration.isTransactionsEnabled(),
             configuration.getTransactionIsolationLevel(), 
             configuration.getTransactionMode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<CacheEntryListenerRegistration<? super K, ? super V>> getCacheEntryListenerRegistrations() {
        return cacheEntryListenerRegistrations;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CacheLoader<K, ? extends V> getCacheLoader() {
        return this.cacheLoader;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CacheWriter<? super K, ? super V> getCacheWriter() {
        return this.cacheWriter;
    }
    
    /**
     * {@inheritDoc}
     */
    public CacheEntryExpiryPolicy<? super K, ? super V> getCacheEntryExpiryPolicy() {
        return this.cacheEntryExpiryPolicy;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IsolationLevel getTransactionIsolationLevel() {
        return this.txnIsolationLevel;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Mode getTransactionMode() {
        return this.txnMode;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isReadThrough() {
        return this.isReadThrough;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isWriteThrough() {
        return this.isWriteThrough;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStoreByValue() {
        return this.isStoreByValue;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isStatisticsEnabled() {
        return this.isStatisticsEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatisticsEnabled(boolean isStatisticsEnabled) {
        this.isStatisticsEnabled = isStatisticsEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTransactionsEnabled() {
        return this.isTransactionsEnabled;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((cacheEntryListenerRegistrations == null) ? 0 : cacheEntryListenerRegistrations
                        .hashCode());
        result = prime * result
                + ((cacheLoader == null) ? 0 : cacheLoader.hashCode());
        result = prime * result
                + ((cacheWriter == null) ? 0 : cacheWriter.hashCode());
        result = prime * result
                + ((cacheEntryExpiryPolicy == null) ? 0 : cacheEntryExpiryPolicy.hashCode());
        result = prime * result + (isReadThrough ? 1231 : 1237);
        result = prime * result + (isStatisticsEnabled ? 1231 : 1237);
        result = prime * result + (isStoreByValue ? 1231 : 1237);
        result = prime * result + (isWriteThrough ? 1231 : 1237);
        result = prime
                * result
                + ((txnIsolationLevel == null) ? 0 : txnIsolationLevel
                        .hashCode());
        result = prime * result + ((txnMode == null) ? 0 : txnMode.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof RICacheConfiguration)) {
            return false;
        }
        RICacheConfiguration<?, ?> other = (RICacheConfiguration<?, ?>) object;
        if (cacheEntryListenerRegistrations == null) {
            if (other.cacheEntryListenerRegistrations != null) {
                return false;
            }
        } else if (!cacheEntryListenerRegistrations.equals(other.cacheEntryListenerRegistrations)) {
            return false;
        }
        if (cacheLoader == null) {
            if (other.cacheLoader != null) {
                return false;
            }
        } else if (!cacheLoader.equals(other.cacheLoader)) {
            return false;
        }
        if (cacheWriter == null) {
            if (other.cacheWriter != null) {
                return false;
            }
        } else if (!cacheWriter.equals(other.cacheWriter)) {
            return false;
        }
        if (cacheEntryExpiryPolicy == null) {
            if (other.cacheEntryExpiryPolicy != null) {
                return false;
            }
        } else if (!cacheEntryExpiryPolicy.equals(other.cacheEntryExpiryPolicy)) {
            return false;
        }
        if (isReadThrough != other.isReadThrough) {
            return false;
        }
        if (isStatisticsEnabled != other.isStatisticsEnabled) {
            return false;
        }
        if (isStoreByValue != other.isStoreByValue) {
            return false;
        }
        if (isWriteThrough != other.isWriteThrough) {
            return false;
        }
        if (isTransactionsEnabled != other.isTransactionsEnabled) {
            return false;
        }
        if (txnIsolationLevel != other.txnIsolationLevel) {
            return false;
        }
        if (txnMode != other.txnMode) {
            return false;
        }
        return true;
    }
}
