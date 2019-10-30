/****************************************************************************
 * Copyright 2019, Optimizely, Inc. and contributors                        *
 *                                                                          *
 * Licensed under the Apache License, Version 2.0 (the "License");          *
 * you may not use this file except in compliance with the License.         *
 * You may obtain a copy of the License at                                  *
 *                                                                          *
 *    http://www.apache.org/licenses/LICENSE-2.0                            *
 *                                                                          *
 * Unless required by applicable law or agreed to in writing, software      *
 * distributed under the License is distributed on an "AS IS" BASIS,        *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. *
 * See the License for the specific language governing permissions and      *
 * limitations under the License.                                           *
 ***************************************************************************/

package com.optimizely.ab.integration_test.app.support.apis;

import com.optimizely.ab.integration_test.app.support.OptimizelyWrapper;
import com.optimizely.ab.integration_test.app.models.apiparams.TrackParams;
import com.optimizely.ab.integration_test.app.models.responses.BaseResponse;
import com.optimizely.ab.integration_test.app.models.responses.ListenerMethodResponse;

public class TrackAPICall extends APICall {

    public TrackAPICall() {
        super();
    }

    @Override
    public BaseResponse invokeAPI(OptimizelyWrapper optimizelyWrapper, Object desreailizeObject) {
        TrackParams trackParams = mapper.convertValue(desreailizeObject, TrackParams.class);
        return track(optimizelyWrapper, trackParams);
    }

    private ListenerMethodResponse<Boolean> track(OptimizelyWrapper optimizelyWrapper, TrackParams trackParams) {

        optimizelyWrapper.getOptimizelyManager().getOptimizely().track(
                trackParams.getEventKey(),
                trackParams.getUserId(),
                trackParams.getAttributes(),
                trackParams.getEventTags()
        );

        return sendResponse(null, optimizelyWrapper);
    }

}