package com.storemax.android.devices.rfcard;

import android.app.Activity;
import com.landicorp.android.eptapi.card.MifareDriver;
import com.landicorp.android.eptapi.card.RFDriver;
import com.landicorp.android.eptapi.card.MifareDriver.OnResultListener;
import com.landicorp.android.eptapi.device.RFCardReader;
import com.landicorp.android.eptapi.exception.RequestException;
import com.landicorp.android.eptapi.utils.BytesUtil;
import com.storemax.android.devices.bean.DeviceExceptionBean;
import com.storemax.android.devices.bean.RFCardAuthBean;
import com.storemax.android.devices.bean.RFCardBlockBean;
import com.storemax.android.devices.device.IDevice;
import com.storemax.android.devices.listener.OnDeviceStateListener;
import com.storemax.android.devices.listener.OnRFCardReadListener;
import com.storemax.android.devices.listener.OnRFCardSearchListener;
import com.storemax.android.devices.listener.OnRFCardWriteListener;

public class LandiRfCard extends BaseRFCard {
	
	/**
	 * 卡内码
	 */
	public String cardNum = "";
	
	private OnRFCardSearchListener listener;
	
	private RFDriver driver;
	
	private IDevice idevice;
	
	public LandiRfCard(IDevice idevice) {
		this.idevice = idevice;
	}
	
	@Override
	public void openDevice(Activity activity, OnDeviceStateListener listener) {
		if (null != idevice) {
			idevice.openDevice(activity, listener);
		}
		
	}
	
	@Override
	public void searchCard(OnRFCardSearchListener listener) throws Exception {
		RFCardReader.getInstance().setParameter(0x0e, 0x00);
		RFCardReader.getInstance().searchCard(onSearchListener);
		this.listener = listener;
	}
	
	protected RFCardReader.OnSearchListener onSearchListener = new RFCardReader.OnSearchListener() {
		
		@Override
		public void onCardPass(int cardType) {
			String driverName = "";
			// 扫描成功处理
			switch (cardType) {
			case S50_CARD:// S50 卡，仅支持 S50 驱动
				driverName = "S50";
				try {
					RFCardReader.getInstance().activate(driverName,
							onActiveListener);
				} catch (RequestException e) {
					e.printStackTrace();
				}
				break;
			// case S70_CARD:// S70
			// // 卡，仅支持
			// // S70
			// // 驱动
			// driverName = "S70";
			// break;
			// case CPU_CARD:// CPU 卡，仅支持 CPU 驱动
			// case PRO_CARD:// PRO 卡，仅支持 PRO 驱动
			// case S50_PRO_CARD:// 支持 S50 驱动和 PRO 驱动的 PRO 卡
			// case S70_PRO_CARD:// 支持 S70 驱动和 PRO 驱动的 PRO 卡
			// driverName = "PRO";
			// break;
			default:
				if (listener != null) {
					listener.onFail(-2, "寻卡失败,该卡不是M1卡", null);
				}
				break;
			}
			
		}
		
		@Override
		public void onFail(int code) {
			if (listener != null) {
				listener.onFail(code, getErrorMsgByCode(code), null);
			}
		}
		
		@Override
		public void onCrash() {
			if (listener != null) {
				listener.onCrash();
			}
		}
	};
	
	// 卡激活监听器
	protected RFCardReader.OnActiveListener onActiveListener = new RFCardReader.OnActiveListener() {
		
		@Override
		public void onCardActivate(final RFDriver driver) {
			LandiRfCard.this.driver = driver;
			byte[] cardNo = getLastCardSerialNo();
			if (null != cardNo) {
				cardNum = BytesUtil.bytes2HexString(cardNo);
				listener.onSuccess();
			} else {
				listener.onFail(BaseRFCard.GET_CARD_NUMBER_ERROR, "射频卡内码获取错误",
						null);
			}
			
		}
		
		@Override
		public void onActivateError(int code) {
			listener.onFail(code, getErrorMsgByCode(code), code);
		}
		
		@Override
		public void onUnsupport(String driverName) {
			listener.onFail(ERROR_CODE_UNSUPPORT, "当前设备不支持该卡！", null);
		}
		
		@Override
		public void onCrash() {
			listener.onCrash();
		}
	};
	
	@Override
	public void readBlock(RFCardAuthBean auth, final RFCardBlockBean block,
			final OnRFCardReadListener listener) {
		
		try {
			((MifareDriver) driver).authSector(auth.getSectorNo(),
					getKeyType(auth.getKeyType()), auth.getPwd(),
					new OnResultListener() {
						
						@Override
						public void onCrash() {
							if (listener != null) {
								listener.onCash();
							}
						}
						
						@Override
						public void onSuccess() {
							
							try {
								((MifareDriver) driver).readBlock(
										block.getBlkNo(),
										new MifareDriver.OnReadListener() {
											
											@Override
											public void onFail(int arg0) {
												if (listener != null) {
													listener.onFail(
															arg0,
															getErrorMsgByCode(arg0),
															null);
												}
											}
											
											@Override
											public void onSuccess(byte[] value1) {
												if (listener != null) {
													listener.onSuccess(value1);
												}
											}
											
											@Override
											public void onCrash() {
												if (listener != null) {
													listener.onCash();
												}
											}
										});
							} catch (RequestException e) {
								if (listener != null) {
									listener.onThrowException(new DeviceExceptionBean(
											e));
								}
								e.printStackTrace();
							}
						}
						
						@Override
						public void onFail(int arg0) {
							if (listener != null) {
								listener.onFail(arg0, getErrorMsgByCode(arg0),
										null);
							}
						}
					});
		} catch (RequestException e) {
			if (listener != null) {
				listener.onThrowException(new DeviceExceptionBean(e));
			}
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void writeBlock(RFCardAuthBean auth, final RFCardBlockBean block,
			final OnRFCardWriteListener listener) {
		
		try {
			((MifareDriver) driver).authSector(auth.getSectorNo(),
					getKeyType(auth.getKeyType()), auth.getPwd(),
					new OnResultListener() {
						
						@Override
						public void onCrash() {
							if (listener != null) {
								listener.onCash();
							}
						}
						
						@Override
						public void onSuccess() {
							
							try {
								((MifareDriver) driver).writeBlock(
										block.getBlkNo(), block.getBlkData(),
										new OnResultListener() {
											
											@Override
											public void onFail(int arg0) {
												if (listener != null) {
													listener.onFail(
															arg0,
															getErrorMsgByCode(arg0),
															null);
												}
											}
											
											@Override
											public void onSuccess() {
												if (listener != null) {
													listener.onSuccess();
												}
											}
											
											@Override
											public void onCrash() {
												if (listener != null) {
													listener.onCash();
												}
											}
										});
							} catch (RequestException e) {
								if (listener != null) {
									listener.onThrowException(new DeviceExceptionBean(
											e));
								}
								e.printStackTrace();
							}
						}
						
						@Override
						public void onFail(int arg0) {
							if (listener != null) {
								listener.onFail(arg0, getErrorMsgByCode(arg0),
										null);
							}
						}
					});
		} catch (RequestException e) {
			if (listener != null) {
				listener.onThrowException(new DeviceExceptionBean(e));
			}
			e.printStackTrace();
		}
	}
	
	@Override
	public void stopDevice() throws Exception {
		RFCardReader.getInstance().stopSearch();
	}
	
	@Override
	public void closeDevice() throws Exception {
		listener = null;
		RFCardReader.getInstance().stopSearch();
		if (null != idevice) {
			idevice.closeDevice();
		}
	}
	
	@Override
	public void writeCardNumber(String cardNo) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String readCardNumber() throws Exception {
		return cardNum;
	}
	
	@Override
	public String getErrorMsgByCode(int code) {
		switch (code) {
		case MifareDriver.OnReadListener.ERROR_ERRPARAM:
			return "参数错误";
		case MifareDriver.OnReadListener.ERROR_FAILED:
			return "其它错误(OS error,etc)";
		case MifareDriver.OnReadListener.ERROR_NOTAGERR:
			return "没有找到卡片或卡片无响应";
		case MifareDriver.OnReadListener.ERROR_CRCERR:
			return "数据CRC检验错误";
		case MifareDriver.OnReadListener.ERROR_AUTHERR:
			return "权限验证失败";
		case MifareDriver.OnReadListener.ERROR_PARITYERR:
			return "数据校验错误";
		case MifareDriver.OnReadListener.ERROR_CODEERR:
			return "卡片响应数据出错";
		case MifareDriver.OnReadListener.ERROR_SERNRERR:
			return "数据处理过程出错";
		case MifareDriver.OnReadListener.ERROR_NOTAUTHERR:
			return "卡片未授权";
		case MifareDriver.OnReadListener.ERROR_BITCOUNTERR:
			return "卡片返回数据的长度错误";
		case MifareDriver.OnReadListener.ERROR_BYTECOUNTERR:
			return "卡返回的数据字节长度错误";
		case MifareDriver.OnReadListener.ERROR_OVFLERR:
			return "卡数据返回溢出";
		case MifareDriver.OnReadListener.ERROR_FRAMINGERR:
			return "数据帧误错误";
		case MifareDriver.OnReadListener.ERROR_UNKNOWN_COMMAND:
			return "终端发送非法命令";
		case MifareDriver.OnReadListener.ERROR_COLLERR:
			return "多张卡片冲突";
		case MifareDriver.OnReadListener.ERROR_RESETERR:
			return "射频卡模块复位失败";
		case MifareDriver.OnReadListener.ERROR_INTERFACEERR:
			return "射频卡模块接口错误";
		case MifareDriver.OnReadListener.ERROR_RECBUF_OVERFLOW:
			return "接收缓冲区溢出";
		case MifareDriver.OnReadListener.ERROR_VALERR:
			return "对Mifare卡操作块，块错误";
		case MifareDriver.OnReadListener.ERROR_ERRTYPE:
			return "卡片错误类型";
		case MifareDriver.OnReadListener.ERROR_SWDIFF:
			return "Data exchange with MifarePro card or TypeB card, card loopback status byte SW1! = 0x90, =0x00 SW2.";
		case MifareDriver.OnReadListener.ERROR_TRANSERR:
			return "通信错误";
		case MifareDriver.OnReadListener.ERROR_PROTERR:
			return "卡片返回数据不符合本协议要求 ";
		case MifareDriver.OnReadListener.ERROR_MULTIERR:
			return "感应区内发现多长卡片";
		case MifareDriver.OnReadListener.ERROR_NOCARD:
			return "感应区内没有卡片";
		case MifareDriver.OnReadListener.ERROR_CARDEXIST:
			return "卡片仍在感应区";
		case MifareDriver.OnReadListener.ERROR_CARDTIMEOUT:
			return "响应超时";
		case MifareDriver.OnReadListener.ERROR_CARDNOACT:
			return "卡片未激活";
		}
		return "未知的错误 [" + code + "]";
	}
	
	public int getKeyType(KeyType type) {
		switch (type) {
		case KEY_A: {
			return MifareDriver.KEY_A;
		}
		case KEY_B: {
			return MifareDriver.KEY_B;
		}
		default:
			return MifareDriver.KEY_A;
		}
	}
	
}
