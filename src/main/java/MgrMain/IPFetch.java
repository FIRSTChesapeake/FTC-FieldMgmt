package MgrMain;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
//import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPFetch {

    final public static Logger logger = LoggerFactory.getLogger(Main.class);

    public List<String> SysIPs = new ArrayList<String>();
    public List<NetworkAdapter> Adapters = new ArrayList<NetworkAdapter>();
    
    public IPFetch() {
//        try {
//            logger.info("Fetching IP Addresses");
//            final InetAddress localhost = InetAddress.getLocalHost();
//            final InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
//            int cnt = 0;
//            if (allMyIps != null) {
//                for (final InetAddress allMyIp : allMyIps) {
//                    SysIPs.add(allMyIp.toString());
//                    cnt++;
//                }
//            }
//            logger.info("Found {} IP Addresses",cnt);
//        } catch (final UnknownHostException e) {
//            logger.info(" (error retrieving server host name)");
//        }

        try {
            logger.info("Fetching Local Adapters");
            int cntI = 0;
            for (final Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                boolean isLoop = false;
                final NetworkInterface intf = en.nextElement();
                NetworkAdapter n = new NetworkAdapter(intf.getDisplayName());
                for (final Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    final InetAddress adde = enumIpAddr.nextElement();
                    if(!adde.isLoopbackAddress()) {
                        n.addIP(adde.toString());
                        cntI++;
                    } else {
                        isLoop = true;
                    }
                }
                // If the adapter isn't loopback and it has at least 1 IP address.
                if(!isLoop && n.IPs.size() > 0) {
                    Adapters.add(n);
                }
            }
            logger.info("Found {} IP Addresses connected to {} Adapters",cntI,Adapters.size());
        } catch (final SocketException e) {
            logger.info(" (error retrieving network interface list)");
        }
    }

    
    
    public void print() {
        logger.info("PRINTING NETWORK INFORMATION");
//        logger.info("ALL IPS:");
//        for(String s : SysIPs) {
//            logger.info("  {}",s);
//        }
//        logger.info("ALL ADAPTERS:");
        for(NetworkAdapter n : Adapters) {
            n.print();
        }
    }
    public class NetworkAdapter {
        public String AdapterName = "";
        public List<String> IPs = new ArrayList<String>();
        
        public void print() {
            logger.info("ADAPTER: {}",AdapterName);
            for(String s : IPs) {
                logger.info("  {}",s);
            }
        }
        
        public void addIP(String IP) {
            IPs.add(IP);
        }
        
        public NetworkAdapter(String Name) {
            AdapterName = Name;
        }
    }
}
