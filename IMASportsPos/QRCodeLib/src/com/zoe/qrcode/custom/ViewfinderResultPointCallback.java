/*
 * Copyright (C) 2009 ZXing authors
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

package com.zoe.qrcode.custom;

import com.zoe.qrcode.ResultPoint;
import com.zoe.qrcode.ResultPointCallback;

/**
 * 结果返回事件
 * 
 * @ClassName: ViewfinderResultPointCallback
 * @author A18ccms a18ccms_gmail_com
 * @date 2015年5月25日 上午9:45:34
 *
 */
final class ViewfinderResultPointCallback implements ResultPointCallback {
	/**
	 * 操作句柄
	 */
	private final ScanBarcodeView viewfinderView;

	/**
	 * 构造函数
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param viewfinderView
	 */
	public ViewfinderResultPointCallback(ScanBarcodeView viewfinderView) {
		this.viewfinderView = viewfinderView;
	}

	@Override
	public void foundPossibleResultPoint(ResultPoint point) {
		viewfinderView.addPossibleResultPoint(point);
	}

}
