/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: frameworks/base/telephony/java/com/android/internal/telephony/ITelephony.aidl
 */
package telephony;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * Interface used to interact with the phone. Mostly this is used by the
 * TelephonyManager class. A few places are still using this directly. Please
 * clean them up if possible and use TelephonyManager insteadl.
 *
 * {@hide}
 */
public interface ITelephony extends IInterface {
	/** Local-side IPC implementation stub class. */
	public static abstract class Stub extends Binder implements
			telephony.ITelephony {
		private static final String DESCRIPTOR = "com.android.internal.telephony.ITelephony";

		/** Construct the stub at attach it to the interface. */
		public Stub() {
			this.attachInterface(this, DESCRIPTOR);
		}

		/**
		 * Cast an IBinder object into an ITelephony interface, generating a
		 * proxy if needed.
		 */
		public static telephony.ITelephony asInterface(IBinder obj) {
			if ((obj == null)) {
				return null;
			}
			IInterface iin = (IInterface) obj.queryLocalInterface(DESCRIPTOR);
			if (((iin != null) && (iin instanceof telephony.ITelephony))) {
				return ((telephony.ITelephony) iin);
			}
			return new telephony.ITelephony.Stub.Proxy(obj);
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			switch (code) {
			case INTERFACE_TRANSACTION: {
				reply.writeString(DESCRIPTOR);
				return true;
			}
			case TRANSACTION_dial: {
				data.enforceInterface(DESCRIPTOR);
				String _arg0;
				_arg0 = data.readString();
				this.dial(_arg0);
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_call: {
				data.enforceInterface(DESCRIPTOR);
				String _arg0;
				_arg0 = data.readString();
				this.call(_arg0);
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_showCallScreen: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.showCallScreen();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_endCall: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.endCall();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_isOffhook: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.isOffhook();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_isRinging: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.isRinging();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_isIdle: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.isIdle();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_isRadioOn: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.isRadioOn();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_isSimPinEnabled: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.isSimPinEnabled();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_cancelMissedCallsNotification: {
				data.enforceInterface(DESCRIPTOR);
				this.cancelMissedCallsNotification();
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_supplyPin: {
				data.enforceInterface(DESCRIPTOR);
				String _arg0;
				_arg0 = data.readString();
				boolean _result = this.supplyPin(_arg0);
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_handlePinMmi: {
				data.enforceInterface(DESCRIPTOR);
				String _arg0;
				_arg0 = data.readString();
				boolean _result = this.handlePinMmi(_arg0);
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_toggleRadioOnOff: {
				data.enforceInterface(DESCRIPTOR);
				this.toggleRadioOnOff();
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_setRadio: {
				data.enforceInterface(DESCRIPTOR);
				boolean _arg0;
				_arg0 = (0 != data.readInt());
				boolean _result = this.setRadio(_arg0);
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_updateServiceLocation: {
				data.enforceInterface(DESCRIPTOR);
				this.updateServiceLocation();
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_enableLocationUpdates: {
				data.enforceInterface(DESCRIPTOR);
				this.enableLocationUpdates();
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_disableLocationUpdates: {
				data.enforceInterface(DESCRIPTOR);
				this.disableLocationUpdates();
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_enableApnType: {
				data.enforceInterface(DESCRIPTOR);
				String _arg0;
				_arg0 = data.readString();
				int _result = this.enableApnType(_arg0);
				reply.writeNoException();
				reply.writeInt(_result);
				return true;
			}
			case TRANSACTION_disableApnType: {
				data.enforceInterface(DESCRIPTOR);
				String _arg0;
				_arg0 = data.readString();
				int _result = this.disableApnType(_arg0);
				reply.writeNoException();
				reply.writeInt(_result);
				return true;
			}
			case TRANSACTION_enableDataConnectivity: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.enableDataConnectivity();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_disableDataConnectivity: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.disableDataConnectivity();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_isDataConnectivityPossible: {
				data.enforceInterface(DESCRIPTOR);
				boolean _result = this.isDataConnectivityPossible();
				reply.writeNoException();
				reply.writeInt(((_result) ? (1) : (0)));
				return true;
			}
			case TRANSACTION_getCellLocation: {
				data.enforceInterface(DESCRIPTOR);
				Bundle _result = this.getCellLocation();
				reply.writeNoException();
				if ((_result != null)) {
					reply.writeInt(1);
					_result.writeToParcel(reply,
							android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
				} else {
					reply.writeInt(0);
				}
				return true;
			}
			case TRANSACTION_getCallState: {
				data.enforceInterface(DESCRIPTOR);
				int _result = this.getCallState();
				reply.writeNoException();
				reply.writeInt(_result);
				return true;
			}
			case TRANSACTION_getDataActivity: {
				data.enforceInterface(DESCRIPTOR);
				int _result = this.getDataActivity();
				reply.writeNoException();
				reply.writeInt(_result);
				return true;
			}
			case TRANSACTION_getDataState: {
				data.enforceInterface(DESCRIPTOR);
				int _result = this.getDataState();
				reply.writeNoException();
				reply.writeInt(_result);
				return true;
			}
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements telephony.ITelephony {
			private IBinder mRemote;

			Proxy(IBinder remote) {
				mRemote = remote;
			}

			public IBinder asBinder() {
				return mRemote;
			}

			public String getInterfaceDescriptor() {
				return DESCRIPTOR;
			}

			/**
			 * Dial a number. This doesn't place the call. It displays the
			 * Dialer screen.
			 * 
			 * @param number
			 *            the number to be dialed. If null, this would display
			 *            the Dialer screen with no number pre-filled.
			 */
			public void dial(String number) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(number);
					mRemote.transact(Stub.TRANSACTION_dial, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			/**
			 * Place a call to the numer.
			 * 
			 * @param number
			 *            the number to be called.
			 */
			public void call(String number) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(number);
					mRemote.transact(Stub.TRANSACTION_call, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			/**
			 * If there is currently a call in progress, show the call screen.
			 * Returns true if the call screen was shown.
			 */
			public boolean showCallScreen() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_showCallScreen, _data,
							_reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * End call or go to the Home screen
			 *
			 * @return whether it hung up
			 */
			public boolean endCall() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_endCall, _data, _reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Check if we are in either an active or holding call
			 * 
			 * @return true if the phone state is OFFHOOK.
			 */
			public boolean isOffhook() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_isOffhook, _data, _reply,
							0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Check if an incoming phone call is ringing or call waiting.
			 * 
			 * @return true if the phone state is RINGING.
			 */
			public boolean isRinging() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_isRinging, _data, _reply,
							0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Check if the phone is idle.
			 * 
			 * @return true if the phone state is IDLE.
			 */
			public boolean isIdle() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_isIdle, _data, _reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Check to see if the radio is on or not.
			 * 
			 * @return returns true if the radio is on.
			 */
			public boolean isRadioOn() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_isRadioOn, _data, _reply,
							0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Check if the SIM pin lock is enabled.
			 * 
			 * @return true if the SIM pin lock is enabled.
			 */
			public boolean isSimPinEnabled() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_isSimPinEnabled, _data,
							_reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Cancels the missed calls notification.
			 */
			public void cancelMissedCallsNotification() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(
							Stub.TRANSACTION_cancelMissedCallsNotification,
							_data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			/**
			 * Supply a pin to unlock the SIM. Blocks until a result is
			 * determined.
			 * 
			 * @param pin
			 *            The pin to check.
			 * @return whether the operation was a success.
			 */
			public boolean supplyPin(String pin) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(pin);
					mRemote.transact(Stub.TRANSACTION_supplyPin, _data, _reply,
							0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Handles PIN MMI commands (PIN/PIN2/PUK/PUK2), which are initiated
			 * without SEND (so <code>dial</code> is not appropriate).
			 *
			 * @param dialString
			 *            the MMI command to be executed.
			 * @return true if MMI command is executed.
			 */
			public boolean handlePinMmi(String dialString)
					throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(dialString);
					mRemote.transact(Stub.TRANSACTION_handlePinMmi, _data,
							_reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Toggles the radio on or off.
			 */
			public void toggleRadioOnOff() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_toggleRadioOnOff, _data,
							_reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			/**
			 * Set the radio to on or off
			 */
			public boolean setRadio(boolean turnOn) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeInt(((turnOn) ? (1) : (0)));
					mRemote.transact(Stub.TRANSACTION_setRadio, _data, _reply,
							0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Request to update location information in service state
			 */
			public void updateServiceLocation() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_updateServiceLocation,
							_data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			/**
			 * Enable location update notifications.
			 */
			public void enableLocationUpdates() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_enableLocationUpdates,
							_data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			/**
			 * Disable location update notifications.
			 */
			public void disableLocationUpdates() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_disableLocationUpdates,
							_data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			/**
			 * Enable a specific APN type.
			 */
			public int enableApnType(String type) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(type);
					mRemote.transact(Stub.TRANSACTION_enableApnType, _data,
							_reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Disable a specific APN type.
			 */
			public int disableApnType(String type) throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(type);
					mRemote.transact(Stub.TRANSACTION_disableApnType, _data,
							_reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Allow mobile data connections.
			 */
			public boolean enableDataConnectivity() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_enableDataConnectivity,
							_data, _reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Disallow mobile data connections.
			 */
			public boolean disableDataConnectivity() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_disableDataConnectivity,
							_data, _reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			/**
			 * Report whether data connectivity is possible.
			 */
			public boolean isDataConnectivityPossible() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				boolean _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(
							Stub.TRANSACTION_isDataConnectivityPossible, _data,
							_reply, 0);
					_reply.readException();
					_result = (0 != _reply.readInt());
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			public Bundle getCellLocation() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				Bundle _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_getCellLocation, _data,
							_reply, 0);
					_reply.readException();
					if ((0 != _reply.readInt())) {
						_result = Bundle.CREATOR.createFromParcel(_reply);
					} else {
						_result = null;
					}
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			public int getCallState() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_getCallState, _data,
							_reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			public int getDataActivity() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_getDataActivity, _data,
							_reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			public int getDataState() throws RemoteException {
				Parcel _data = Parcel.obtain();
				Parcel _reply = Parcel.obtain();
				int _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_getDataState, _data,
							_reply, 0);
					_reply.readException();
					_result = _reply.readInt();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}
		}

		static final int TRANSACTION_dial = (IBinder.FIRST_CALL_TRANSACTION + 0);
		static final int TRANSACTION_call = (IBinder.FIRST_CALL_TRANSACTION + 1);
		static final int TRANSACTION_showCallScreen = (IBinder.FIRST_CALL_TRANSACTION + 2);
		static final int TRANSACTION_endCall = (IBinder.FIRST_CALL_TRANSACTION + 3);
		static final int TRANSACTION_isOffhook = (IBinder.FIRST_CALL_TRANSACTION + 4);
		static final int TRANSACTION_isRinging = (IBinder.FIRST_CALL_TRANSACTION + 5);
		static final int TRANSACTION_isIdle = (IBinder.FIRST_CALL_TRANSACTION + 6);
		static final int TRANSACTION_isRadioOn = (IBinder.FIRST_CALL_TRANSACTION + 7);
		static final int TRANSACTION_isSimPinEnabled = (IBinder.FIRST_CALL_TRANSACTION + 8);
		static final int TRANSACTION_cancelMissedCallsNotification = (IBinder.FIRST_CALL_TRANSACTION + 9);
		static final int TRANSACTION_supplyPin = (IBinder.FIRST_CALL_TRANSACTION + 10);
		static final int TRANSACTION_handlePinMmi = (IBinder.FIRST_CALL_TRANSACTION + 11);
		static final int TRANSACTION_toggleRadioOnOff = (IBinder.FIRST_CALL_TRANSACTION + 12);
		static final int TRANSACTION_setRadio = (IBinder.FIRST_CALL_TRANSACTION + 13);
		static final int TRANSACTION_updateServiceLocation = (IBinder.FIRST_CALL_TRANSACTION + 14);
		static final int TRANSACTION_enableLocationUpdates = (IBinder.FIRST_CALL_TRANSACTION + 15);
		static final int TRANSACTION_disableLocationUpdates = (IBinder.FIRST_CALL_TRANSACTION + 16);
		static final int TRANSACTION_enableApnType = (IBinder.FIRST_CALL_TRANSACTION + 17);
		static final int TRANSACTION_disableApnType = (IBinder.FIRST_CALL_TRANSACTION + 18);
		static final int TRANSACTION_enableDataConnectivity = (IBinder.FIRST_CALL_TRANSACTION + 19);
		static final int TRANSACTION_disableDataConnectivity = (IBinder.FIRST_CALL_TRANSACTION + 20);
		static final int TRANSACTION_isDataConnectivityPossible = (IBinder.FIRST_CALL_TRANSACTION + 21);
		static final int TRANSACTION_getCellLocation = (IBinder.FIRST_CALL_TRANSACTION + 22);
		static final int TRANSACTION_getCallState = (IBinder.FIRST_CALL_TRANSACTION + 23);
		static final int TRANSACTION_getDataActivity = (IBinder.FIRST_CALL_TRANSACTION + 24);
		static final int TRANSACTION_getDataState = (IBinder.FIRST_CALL_TRANSACTION + 25);
	}

	/**
	 * Dial a number. This doesn't place the call. It displays the Dialer
	 * screen.
	 * 
	 * @param number
	 *            the number to be dialed. If null, this would display the
	 *            Dialer screen with no number pre-filled.
	 */
	public void dial(String number) throws RemoteException;

	/**
	 * Place a call to the numer.
	 * 
	 * @param number
	 *            the number to be called.
	 */
	public void call(String number) throws RemoteException;

	/**
	 * If there is currently a call in progress, show the call screen. Returns
	 * true if the call screen was shown.
	 */
	public boolean showCallScreen() throws RemoteException;

	/**
	 * End call or go to the Home screen
	 *
	 * @return whether it hung up
	 */
	public boolean endCall() throws RemoteException;

	/**
	 * Check if we are in either an active or holding call
	 * 
	 * @return true if the phone state is OFFHOOK.
	 */
	public boolean isOffhook() throws RemoteException;

	/**
	 * Check if an incoming phone call is ringing or call waiting.
	 * 
	 * @return true if the phone state is RINGING.
	 */
	public boolean isRinging() throws RemoteException;

	/**
	 * Check if the phone is idle.
	 * 
	 * @return true if the phone state is IDLE.
	 */
	public boolean isIdle() throws RemoteException;

	/**
	 * Check to see if the radio is on or not.
	 * 
	 * @return returns true if the radio is on.
	 */
	public boolean isRadioOn() throws RemoteException;

	/**
	 * Check if the SIM pin lock is enabled.
	 * 
	 * @return true if the SIM pin lock is enabled.
	 */
	public boolean isSimPinEnabled() throws RemoteException;

	/**
	 * Cancels the missed calls notification.
	 */
	public void cancelMissedCallsNotification() throws RemoteException;

	/**
	 * Supply a pin to unlock the SIM. Blocks until a result is determined.
	 * 
	 * @param pin
	 *            The pin to check.
	 * @return whether the operation was a success.
	 */
	public boolean supplyPin(String pin) throws RemoteException;

	/**
	 * Handles PIN MMI commands (PIN/PIN2/PUK/PUK2), which are initiated without
	 * SEND (so <code>dial</code> is not appropriate).
	 *
	 * @param dialString
	 *            the MMI command to be executed.
	 * @return true if MMI command is executed.
	 */
	public boolean handlePinMmi(String dialString) throws RemoteException;

	/**
	 * Toggles the radio on or off.
	 */
	public void toggleRadioOnOff() throws RemoteException;

	/**
	 * Set the radio to on or off
	 */
	public boolean setRadio(boolean turnOn) throws RemoteException;

	/**
	 * Request to update location information in service state
	 */
	public void updateServiceLocation() throws RemoteException;

	/**
	 * Enable location update notifications.
	 */
	public void enableLocationUpdates() throws RemoteException;

	/**
	 * Disable location update notifications.
	 */
	public void disableLocationUpdates() throws RemoteException;

	/**
	 * Enable a specific APN type.
	 */
	public int enableApnType(String type) throws RemoteException;

	/**
	 * Disable a specific APN type.
	 */
	public int disableApnType(String type) throws RemoteException;

	/**
	 * Allow mobile data connections.
	 */
	public boolean enableDataConnectivity() throws RemoteException;

	/**
	 * Disallow mobile data connections.
	 */
	public boolean disableDataConnectivity() throws RemoteException;

	/**
	 * Report whether data connectivity is possible.
	 */
	public boolean isDataConnectivityPossible() throws RemoteException;

	public Bundle getCellLocation() throws RemoteException;

	public int getCallState() throws RemoteException;

	public int getDataActivity() throws RemoteException;

	public int getDataState() throws RemoteException;
}
