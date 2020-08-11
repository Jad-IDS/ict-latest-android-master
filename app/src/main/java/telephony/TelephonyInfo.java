package telephony;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;

import android.telephony.TelephonyManager;

public final class TelephonyInfo {

	private static TelephonyInfo telephonyInfo;
	private String imsiSIM1, imeiSIM1, carrier1;
	private String imsiSIM2, imeiSIM2, carrier2;
	private boolean isSIM1Ready;
	private boolean isSIM2Ready;

	public String getCarrier1() {
		return carrier1;
	}

	public String getCarrier2() {
		return carrier2;
	}

	public String getImeiSIM1() {
		return imeiSIM1;
	}

	public String getImeiSIM2() {
		return imeiSIM2;
	}

	public String getImsiSIM1() {
		return imsiSIM1;
	}

	/*
	 * public static void setImsiSIM1(String imsiSIM1) { TelephonyInfo.imsiSIM1
	 * = imsiSIM1; }
	 */

	public String getImsiSIM2() {
		return imsiSIM2;
	}

	/*
	 * public static void setImsiSIM2(String imsiSIM2) { TelephonyInfo.imsiSIM2
	 * = imsiSIM2; }
	 */

	public boolean isSIM1Ready() {
		return isSIM1Ready;
	}

	/*
	 * public static void setSIM1Ready(boolean isSIM1Ready) {
	 * TelephonyInfo.isSIM1Ready = isSIM1Ready; }
	 */

	public boolean isSIM2Ready() {
		return isSIM2Ready;
	}

	/*
	 * public static void setSIM2Ready(boolean isSIM2Ready) {
	 * TelephonyInfo.isSIM2Ready = isSIM2Ready; }
	 */

	public boolean isDualSIM() {
		return imsiSIM2 != null;
	}

	private TelephonyInfo() {
	}

	public static TelephonyInfo getInstance(Context context) {

		try {
			if (telephonyInfo == null) {

				telephonyInfo = new TelephonyInfo();

				TelephonyManager telephonyManager = ((TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE));
				try {
					telephonyInfo.imsiSIM1 = telephonyManager.getDeviceId();
					;
				} catch (Exception e) {
					telephonyInfo.imsiSIM1 = "";
				}
				telephonyInfo.imsiSIM2 = null;
				try {
					telephonyInfo.imsiSIM1 = getDeviceIdBySlotTelephony(
							context, "getDefault", 0).getSubscriberId();
					telephonyInfo.imsiSIM2 = getDeviceIdBySlotTelephony(
							context, "getDefault", 1).getSubscriberId();
					telephonyInfo.carrier1 = getDeviceIdBySlotTelephony(
							context, "getDefault", 0).getNetworkOperatorName();
					telephonyInfo.carrier2 = getDeviceIdBySlotTelephony(
							context, "getDefault", 1).getNetworkOperatorName();
				} catch (Exception e) {
					try {
						telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context,
								"getDeviceIdGemini", 0);
						telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context,
								"getDeviceIdGemini", 1);
						telephonyInfo.carrier1 = getDeviceIdBySlot(context,
								"getNetworkOperatorName", 0);
						telephonyInfo.carrier2 = getDeviceIdBySlot(context,
								"getNetworkOperatorName", 1);
					} catch (GeminiMethodNotFoundException e1) {
						e1.printStackTrace();
						try {
							telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context,
									"getSubscriberId", 0);
							telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context,
									"getSubscriberId", 1);
							telephonyInfo.carrier1 = getDeviceIdBySlot(context,
									"getNetworkOperatorName", 0);
							telephonyInfo.carrier2 = getDeviceIdBySlot(context,
									"getNetworkOperatorName", 1);
						} catch (GeminiMethodNotFoundException w1) {
							// Call here for next manufacturer's predicted
							// method name if you wish
							w1.printStackTrace();
							telephonyInfo.imsiSIM1 = "";
							telephonyInfo.imsiSIM2 = "";
							telephonyInfo.carrier1 = "";
							telephonyInfo.carrier2 = "";
						}
					}

				}
				if (telephonyInfo.carrier1 != null)
					if (telephonyInfo.carrier1.equals("")) {
						try {
							telephonyInfo.carrier1 = getDeviceIdBySlotTelephony(
									context, "getDefault", 0)
									.getSimOperatorName();
							telephonyInfo.carrier2 = getDeviceIdBySlotTelephony(
									context, "getDefault", 1)
									.getSimOperatorName();
						} catch (Exception e) {
							telephonyInfo.carrier2 = "";
							telephonyInfo.carrier1 = "";
						}
					}
				try {
					telephonyInfo.imeiSIM1 = getDeviceIdBySlotTelephony(
							context, "getDefault", 0).getDeviceId();
					telephonyInfo.imeiSIM2 = getDeviceIdBySlotTelephony(
							context, "getDefault", 1).getDeviceId();
				} catch (Exception e) {
					try {
						telephonyInfo.imeiSIM1 = getDeviceIdBySlot(context,
								"getDeviceId", 0);
						telephonyInfo.imeiSIM2 = getDeviceIdBySlot(context,
								"getDeviceId", 1);
					} catch (GeminiMethodNotFoundException e1) {
						e1.printStackTrace();
						telephonyInfo.imeiSIM1 = "";
						telephonyInfo.imeiSIM2 = "";

					}

				}

				try {
					telephonyInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
					telephonyInfo.isSIM2Ready = false;
					telephonyInfo.isSIM1Ready = getSIMStateBySlot(context,
							"getSimStateGemini", 0);
					telephonyInfo.isSIM2Ready = getSIMStateBySlot(context,
							"getSimStateGemini", 1);
				} catch (GeminiMethodNotFoundException e) {

					e.printStackTrace();

					try {
						telephonyInfo.isSIM1Ready = getSIMStateBySlot(context,
								"getSimState", 0);
						telephonyInfo.isSIM2Ready = getSIMStateBySlot(context,
								"getSimState", 1);
					} catch (GeminiMethodNotFoundException e1) {
						// Call here for next manufacturer's predicted method
						// name if you wish
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			// Actions.CreateDialog((Activity)
			// context,"Error in carrier",false);
		}
		return telephonyInfo;
	}

	private static TelephonyManager getDeviceIdBySlotTelephony(Context context,
			String predictedMethodName, int slotID) {

		TelephonyManager imei = null;

		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		try {

			Class<?> telephonyClass = Class.forName(telephony.getClass()
					.getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimID = telephonyClass.getMethod(predictedMethodName,
					parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimID.invoke(telephony, obParameter);

			if (ob_phone != null) {
				imei = (TelephonyManager) ob_phone;

			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return imei;
	}

	private static String getDeviceIdBySlot(Context context,
			String predictedMethodName, int slotID)
			throws GeminiMethodNotFoundException {

		String imsi = null;

		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		try {

			Class<?> telephonyClass = Class.forName(telephony.getClass()
					.getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimID = telephonyClass.getMethod(predictedMethodName,
					parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimID.invoke(telephony, obParameter);

			if (ob_phone != null) {
				imsi = ob_phone.toString();

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return imsi;
	}

	private static boolean getSIMStateBySlot(Context context,
			String predictedMethodName, int slotID)
			throws GeminiMethodNotFoundException {

		boolean isReady = false;

		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		try {

			Class<?> telephonyClass = Class.forName(telephony.getClass()
					.getName());

			Class<?>[] parameter = new Class[1];
			parameter[0] = int.class;
			Method getSimStateGemini = telephonyClass.getMethod(
					predictedMethodName, parameter);

			Object[] obParameter = new Object[1];
			obParameter[0] = slotID;
			Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

			if (ob_phone != null) {
				int simState = Integer.parseInt(ob_phone.toString());
				if (simState == TelephonyManager.SIM_STATE_READY) {
					isReady = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new GeminiMethodNotFoundException(predictedMethodName);
		}

		return isReady;
	}

	private static class GeminiMethodNotFoundException extends Exception {

		private static final long serialVersionUID = -996812356902545308L;

		public GeminiMethodNotFoundException(String info) {
			super(info);
		}
	}
}