/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.cxf.tracing.brave;

import brave.Tracing;
import brave.http.HttpTracing;
import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Provider;
import org.apache.cxf.annotations.Provider.Scope;
import org.apache.cxf.annotations.Provider.Type;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

@NoJSR250Annotations
@Provider(value = Type.Feature, scope = Scope.Client)
public class BraveClientFeature extends AbstractFeature {
    private BraveClientStartInterceptor out;
    private BraveClientStopInterceptor in;

    public BraveClientFeature(final Tracing tracing) {
        this(
          HttpTracing
              .newBuilder(tracing)
              .clientParser(new HttpClientSpanParser())
              .build()
         );
    }
    
    public BraveClientFeature(HttpTracing brave) {
        out = new BraveClientStartInterceptor(brave);
        in = new BraveClientStopInterceptor(brave);
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, Bus bus) {
        provider.getInInterceptors().add(in);
        provider.getOutInterceptors().add(out);
    }
}
