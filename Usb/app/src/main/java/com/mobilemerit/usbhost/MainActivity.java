package com.mobilemerit.usbhost;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;

public class MainActivity extends Activity implements OnClickListener {
    Button btnCheck;
    TextView textInfo;
    EditText textValue;
    Button btSend;
    UsbDevice device;
    UsbManager manager;
    UsbDeviceConnection usbDeviceConnection;
    UsbInterface usbInterface;
    StringBuilder sb;
    UsbEndpoint endpointIn;
    UsbEndpoint endpointOut;

    private static final String ACTION_USB_PERMISSION = "com.mobilemerit.usbhost.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCheck = (Button) findViewById(R.id.bt_check);
        textInfo = (TextView) findViewById(R.id.tv_info);
        textValue = (EditText) findViewById(R.id.et_Text_value);
        btSend = (Button) findViewById(R.id.bt_send);
        btnCheck.setOnClickListener(this);
        btSend.setOnClickListener(this);
        sb = new StringBuilder();
        requestingToConnectDevice();
    }

    private void requestingToConnectDevice() {
        manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        device = deviceIterator.next();
        /*
         * this block required if you need to communicate to USB devices it's
		 * take permission to device
		 * if you want than you can set this to which device you want to communicate
		 */

        final BroadcastReceiver abcd = new BroadcastReceiver() {

            public void onReceive(Context context, Intent myintent) {
                String mypermission = myintent.getAction();
                if (ACTION_USB_PERMISSION.equals(mypermission)) {
                    synchronized (this) {
                        final UsbDevice usbDevice = (UsbDevice) myintent.getParcelableExtra(manager.EXTRA_DEVICE);


                        //PERMISSION given
                        if (myintent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                            if (usbDevice != null) {
                               device = usbDevice;
                            }
                        } else { // if user says no
                            Toast.makeText(getApplicationContext(), " User has denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        };

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter iff = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(abcd, iff);
        manager.requestPermission(device, pi);
    }

    private void checkInfo(UsbDevice usbDevice) {
        textInfo.setText(readDevice(usbDevice));
        btSend.setVisibility(View.VISIBLE);
        textValue.setVisibility(View.VISIBLE);
        btSend.setOnClickListener(this);
    }


    private String readDevice(UsbDevice device) {
        sb.append("Device Name: " + device.getDeviceName() + "\n");
        sb.append(String.format(
                "Device Class: %s -> Subclass: 0x%02x -> Protocol: 0x%02x\n",
                nameForClass(device.getDeviceClass()),
                device.getDeviceSubclass(), device.getDeviceProtocol()));

        for (int i = 0; i < device.getInterfaceCount(); i++) {
            UsbInterface intf = device.getInterface(i);
            sb.append(String
                    .format("+--Interface %d Class: %s -> Subclass: 0x%02x -> Protocol: 0x%02x\n",
                            intf.getId(),
                            nameForClass(intf.getInterfaceClass()),
                            intf.getInterfaceSubclass(),
                            intf.getInterfaceProtocol()));

            for (int j = 0; j < intf.getEndpointCount(); j++) {
                UsbEndpoint endpoint = intf.getEndpoint(j);
                sb.append(String.format("  +---Endpoint %d: %s %s\n",
                        endpoint.getEndpointNumber(),
                        nameForEndpointType(endpoint.getType()),
                        nameForDirection(endpoint.getDirection())));
            }
        }

        return sb.toString();
    }

    /* Helper Methods to Provide Readable Names for USB Constants */
    private String nameForClass(int classType) {
        switch (classType) {
            case UsbConstants.USB_CLASS_APP_SPEC:
                return String.format("Application Specific 0x%02x", classType);
            case UsbConstants.USB_CLASS_AUDIO:
                return "Audio";
            case UsbConstants.USB_CLASS_CDC_DATA:
                return "CDC Control";
            case UsbConstants.USB_CLASS_COMM:
                return "Communications";
            case UsbConstants.USB_CLASS_CONTENT_SEC:
                return "Content Security";
            case UsbConstants.USB_CLASS_CSCID:
                return "Content Smart Card";
            case UsbConstants.USB_CLASS_HID:
                return "Human Interface Device";
            case UsbConstants.USB_CLASS_HUB:
                return "Hub";
            case UsbConstants.USB_CLASS_MASS_STORAGE:
                return "Mass Storage";
            case UsbConstants.USB_CLASS_MISC:
                return "Wireless Miscellaneous";
            case UsbConstants.USB_CLASS_PER_INTERFACE:
                return "(Defined Per Interface)";
            case UsbConstants.USB_CLASS_PHYSICA:
                return "Physical";
            case UsbConstants.USB_CLASS_PRINTER:
                return "Printer";
            case UsbConstants.USB_CLASS_STILL_IMAGE:
                return "Still Image";
            case UsbConstants.USB_CLASS_VENDOR_SPEC:
                return String.format("Vendor Specific 0x%02x", classType);
            case UsbConstants.USB_CLASS_VIDEO:
                return "Video";
            case UsbConstants.USB_CLASS_WIRELESS_CONTROLLER:
                return "Wireless Controller";
            default:
                return String.format("0x%02x", classType);
        }
    }

    private String nameForEndpointType(int type) {
        switch (type) {
            case UsbConstants.USB_ENDPOINT_XFER_BULK:
                return "Bulk";
            case UsbConstants.USB_ENDPOINT_XFER_CONTROL:
                return "Control";
            case UsbConstants.USB_ENDPOINT_XFER_INT:
                return "Interrupt";
            case UsbConstants.USB_ENDPOINT_XFER_ISOC:
                return "Isochronous";
            default:
                return "Unknown Type";
        }
    }

    private String nameForDirection(int direction) {
        switch (direction) {
            case UsbConstants.USB_DIR_IN:
                return "IN";
            case UsbConstants.USB_DIR_OUT:
                return "OUT";
            default:
                return "Unknown Direction";
        }
    }

    @Override
    public void onClick(View view) {
        if(view.equals(btnCheck)){
            checkInfo(device);
        }
        else {
            setDeviceInterface(device);
        }
    }

    private void setDeviceInterface(UsbDevice device) {
        usbInterface = null;
        endpointOut = null;
        endpointIn = null;

        for (int i = 0; i < device.getInterfaceCount(); i++) {
            UsbInterface usbif = device.getInterface(i);

            UsbEndpoint tOut = null;
            UsbEndpoint tIn = null;

            int tEndpointCnt = usbif.getEndpointCount();
            if (tEndpointCnt >= 2) {
                for (int j = 0; j < tEndpointCnt; j++) {
                    if (usbif.getEndpoint(j).getType()
                            == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                        if (usbif.getEndpoint(j).getDirection()
                                == UsbConstants.USB_DIR_OUT) {
                            tOut = usbif.getEndpoint(j);
                        } else if (usbif.getEndpoint(j).getDirection()
                                == UsbConstants.USB_DIR_IN) {
                            tIn = usbif.getEndpoint(j);
                        }
                    }
                }

                if (tOut != null && tIn != null) {
                    // This interface have both USB_DIR_OUT
                    // and USB_DIR_IN of USB_ENDPOINT_XFER_BULK
                    usbInterface = usbif;
                    endpointOut = tOut;
                    endpointIn = tIn;
                }
            }
        }

        final byte[] byteArray;
        // desired connection

        try {
            usbDeviceConnection = manager.openDevice(device);
            boolean flag =  usbDeviceConnection.claimInterface(usbInterface, true);
            String text = textValue.getText().toString();
            byteArray = text.getBytes();

            // / data transfer
            int a = usbDeviceConnection.bulkTransfer(endpointOut, byteArray, byteArray.length, 0);

            if(a>0){
                textInfo.setText("Data has been transferred successfully..."
                                 +"\n"+"Length of data: "+a
                );
                textInfo.setTextSize(50.0f);
                btnCheck.setVisibility(View.GONE);
                textValue.setVisibility(View.GONE);
                btSend.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            textInfo.setText(e.getMessage());
        }
    }
}