/**
 * Copyright (c) 2013 Cangol
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.service.status;

import android.content.Context;

public interface StatusListener {
    /**
     * 网络已连接
     */
    void networkConnect(Context context);

    /**
     * 网络连接中断
     */
    void networkDisconnect(Context context);

    /**
     * 连接到手机网络
     */
    void networkTo3G(Context context);

    /**
     * 外置存储移除
     */
    void storageRemove(Context context);

    /**
     * 外置存储挂载
     */
    void storageMount(Context context);

    /**
     * 呼叫状态
     */
    void callStateIdle();

    /**
     * 挂起状态
     */
    void callStateOffhook();

    /**
     * 响铃状态
     */
    void callStateRinging();
}
