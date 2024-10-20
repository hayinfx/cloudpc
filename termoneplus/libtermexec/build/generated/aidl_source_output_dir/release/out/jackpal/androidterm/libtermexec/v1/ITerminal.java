/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package jackpal.androidterm.libtermexec.v1;
// see also:
// the (clumsy) way to handle object inheritance with Binder:
// https://kevinhartman.github.io/blog/2012/07/23/inheritance-through-ipc-using-aidl-in-android/
// some (possibly outdated) notes on preserving backward compatibility:
// https://stackoverflow.com/questions/18197783/android-aidl-interface-parcelables-and-backwards-compatibility
/**
 * An interface for interacting with Terminal implementation.
 * 
 * The version of the interface is encoded in Intent action and the AIDL package name. New versions
 * of this interface may be implemented in future. Those versions will be made available
 * in separate packages and older versions will continue to work.
 */
public interface ITerminal extends android.os.IInterface
{
  /** Default implementation for ITerminal. */
  public static class Default implements jackpal.androidterm.libtermexec.v1.ITerminal
  {
    /**
     * Start a new Terminal session. A session will remain hosted by service, that provides binding,
     * but no gurantees of process pesistence as well as stability of connection are made. You
     * should keep your ParcelFileDescriptor around and allow ServiceConnection to call this method
     * again, when reconnection happens, in case service hosting the session is killed by system.
     * 
     * Allows caller to be notified of terminal session events. Multiple calls can happen on each,
     * and new call types can be introduced, so prepare to ignore unknown event codes.
     * 
     * So far only notifications about session end (code 0) are supported. This notification is
     * issued after abovementioned file descriptor is closed and the session is ended from
     * Terminal's standpoint.
     * 
     * @param pseudoTerminalMultiplexerFd file descriptor, obtained by opening /dev/ptmx.
     * @param a callback
     * 
     * @return IntentSender, that can be used to start corresponding Terminal Activity.
     */
    @Override public android.content.IntentSender startSession(android.os.ParcelFileDescriptor pseudoTerminalMultiplexerFd, android.os.ResultReceiver callback) throws android.os.RemoteException
    {
      return null;
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements jackpal.androidterm.libtermexec.v1.ITerminal
  {
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an jackpal.androidterm.libtermexec.v1.ITerminal interface,
     * generating a proxy if needed.
     */
    public static jackpal.androidterm.libtermexec.v1.ITerminal asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof jackpal.androidterm.libtermexec.v1.ITerminal))) {
        return ((jackpal.androidterm.libtermexec.v1.ITerminal)iin);
      }
      return new jackpal.androidterm.libtermexec.v1.ITerminal.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      if (code >= android.os.IBinder.FIRST_CALL_TRANSACTION && code <= android.os.IBinder.LAST_CALL_TRANSACTION) {
        data.enforceInterface(descriptor);
      }
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
      }
      switch (code)
      {
        case TRANSACTION_startSession:
        {
          android.os.ParcelFileDescriptor _arg0;
          _arg0 = _Parcel.readTypedObject(data, android.os.ParcelFileDescriptor.CREATOR);
          android.os.ResultReceiver _arg1;
          _arg1 = _Parcel.readTypedObject(data, android.os.ResultReceiver.CREATOR);
          android.content.IntentSender _result = this.startSession(_arg0, _arg1);
          reply.writeNoException();
          _Parcel.writeTypedObject(reply, _result, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
          break;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
      return true;
    }
    private static class Proxy implements jackpal.androidterm.libtermexec.v1.ITerminal
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      /**
       * Start a new Terminal session. A session will remain hosted by service, that provides binding,
       * but no gurantees of process pesistence as well as stability of connection are made. You
       * should keep your ParcelFileDescriptor around and allow ServiceConnection to call this method
       * again, when reconnection happens, in case service hosting the session is killed by system.
       * 
       * Allows caller to be notified of terminal session events. Multiple calls can happen on each,
       * and new call types can be introduced, so prepare to ignore unknown event codes.
       * 
       * So far only notifications about session end (code 0) are supported. This notification is
       * issued after abovementioned file descriptor is closed and the session is ended from
       * Terminal's standpoint.
       * 
       * @param pseudoTerminalMultiplexerFd file descriptor, obtained by opening /dev/ptmx.
       * @param a callback
       * 
       * @return IntentSender, that can be used to start corresponding Terminal Activity.
       */
      @Override public android.content.IntentSender startSession(android.os.ParcelFileDescriptor pseudoTerminalMultiplexerFd, android.os.ResultReceiver callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        android.content.IntentSender _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _Parcel.writeTypedObject(_data, pseudoTerminalMultiplexerFd, 0);
          _Parcel.writeTypedObject(_data, callback, 0);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startSession, _data, _reply, 0);
          _reply.readException();
          _result = _Parcel.readTypedObject(_reply, android.content.IntentSender.CREATOR);
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
    }
    static final int TRANSACTION_startSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
  }
  public static final java.lang.String DESCRIPTOR = "jackpal.androidterm.libtermexec.v1.ITerminal";
  /**
   * Start a new Terminal session. A session will remain hosted by service, that provides binding,
   * but no gurantees of process pesistence as well as stability of connection are made. You
   * should keep your ParcelFileDescriptor around and allow ServiceConnection to call this method
   * again, when reconnection happens, in case service hosting the session is killed by system.
   * 
   * Allows caller to be notified of terminal session events. Multiple calls can happen on each,
   * and new call types can be introduced, so prepare to ignore unknown event codes.
   * 
   * So far only notifications about session end (code 0) are supported. This notification is
   * issued after abovementioned file descriptor is closed and the session is ended from
   * Terminal's standpoint.
   * 
   * @param pseudoTerminalMultiplexerFd file descriptor, obtained by opening /dev/ptmx.
   * @param a callback
   * 
   * @return IntentSender, that can be used to start corresponding Terminal Activity.
   */
  public android.content.IntentSender startSession(android.os.ParcelFileDescriptor pseudoTerminalMultiplexerFd, android.os.ResultReceiver callback) throws android.os.RemoteException;
  /** @hide */
  static class _Parcel {
    static private <T> T readTypedObject(
        android.os.Parcel parcel,
        android.os.Parcelable.Creator<T> c) {
      if (parcel.readInt() != 0) {
          return c.createFromParcel(parcel);
      } else {
          return null;
      }
    }
    static private <T extends android.os.Parcelable> void writeTypedObject(
        android.os.Parcel parcel, T value, int parcelableFlags) {
      if (value != null) {
        parcel.writeInt(1);
        value.writeToParcel(parcel, parcelableFlags);
      } else {
        parcel.writeInt(0);
      }
    }
  }
}
