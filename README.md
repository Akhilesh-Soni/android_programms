# android_programms
This code describes the functionality of Usb host API like how to identify the main property of Usb device connected to Android powered device and how to send Data between Android Powered device and Usb device.


Steps we need to establish Communication between two devices

#### 1. Android Manifest Requirement:

Include a element <uses-feature> that declares that our the application uses the “android.hardware.usb.host feature”.
Set the minimum SDK level 12 because Usb host API is not present in earlier version.

#### 2. Obtain the instance of UsbManager:

By using the following command we can obtain the instance of UsbManager:
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        
        
#### 3. Discover the device:

Using “manager.getDeviceList ()” we can obtain the usb devices list connected to android phone, which return hash map of devices.
By iterating over hash map we can obtain the instance of UsbDevice class of desired device to which we want to communicate.

             UsbDevice device = deviceIterator.next ();


#### 4. Obtain permission to communicate with device:

Using Broadcast receiver we have to obtain the permission from user to access the usb device. Which will return instance of UsbDevice class in extras field of intent, if the user allow to access it.


#### 5. Communicating with device:

From UsbDevice class object we have to find out appropriate instance of UsbInterface, which have two endpoint at least.
From instance of UsbInterface we have to find out the endpoints, one with IN direction, other with OUT direction.

Now we have to claim the interface then we can transfer the data using bulkTransfer () method, like as follows:

          connection.bulkTransfer (endpoint, bytes, bytes.length, TIMEOUT);
If the direction of endpoint is OUT direction then the data will be sent to usb device, and if transfer is succeeded we will get the length of data that has been transferred.

If the direction of endpoint is IN direction then the data will be read from usb device and if transfer is succeeded we will get the length of data that has been received and the value.

After Completing the desired task we have to terminate the connection using broadcast receiver.

#### Reference


http://developer.android.com/guide/topics/connectivity/usb/index.html


https://www.youtube.com/watch?v=s7szcpXf2rE


http://www.beyondlogic.org/usbnutshell/usb1.shtml
