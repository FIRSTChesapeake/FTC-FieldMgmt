FTC-FieldMgmt
=============

FTC Field Mgmt System

You'll need to edit the Windows Registry on the FCS computer(s).
(Eventually I'll make a tool for this.)

Under:
\HKCU\Software\FIRST\samofcs\Preferences

Set these values: (using Decimal format where applicable).
Password          = apple
SS FtpPort        = 2211
SS Integration    = 1
SS IPAddr         = [set this to the IP of the machine running this software.]
SS Key 1          = [Set this to field number, 1 or 2]
SS MsgPort        = 2212
Username          = [Either 'field1' or 'field2']
