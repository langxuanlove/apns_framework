/*
 * Copyright 2013 DiscoveryBay Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kk.apns;

import java.util.List;

import com.kk.apns.model.Feedback;
import com.kk.apns.model.Payload;
import com.kk.apns.model.PushNotification;
public interface IApnsService {
	/**
	 * @param token  deviceToken
	 * @param payload
	 */
	public void sendNotification(String token, Payload payload);
	/**
	 * If you want to specify the ID of a notification, use this method
	 * @param notification
	 */
	public void sendNotification(PushNotification notification);
	
	public void shutdown();
	/**
	 * EN: You should call this interface once an hour, once a day or other time as you wish
	 * CN: 返回用户在设备上卸载了APP的device token。这个接口最好定期调用，比如一天一次，或者一小时一次等等
	 * @return the device tokens which belong to the app that doesn't exist on the device.
	 */
	public List<Feedback> getFeedbacks();
}
