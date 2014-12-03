package MgrMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
// import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPFetch {

    public static class InfoPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        JLabel lbl = new JLabel("System IPs:");
        JLabel ooo = new JLabel("Waiting..");

        public InfoPanel() {
            this.setLayout(new GridLayout(0, 1, 0, 0));
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.add(this.ooo, BorderLayout.CENTER);
        }

        public InfoPanel(final List<NetworkAdapter> Adptrs) {
            this.setLayout(new GridLayout(0, 0, 0, 0));
            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            this.UpdateList(Adptrs);
        }

        public void UpdateList(final List<NetworkAdapter> Adptrs) {
            this.removeAll();
            this.add(this.lbl, BorderLayout.CENTER);
            for (final NetworkAdapter n : Adptrs) {
                for (final String s : n.IPs) {
                    final JLabel l = new JLabel(n.AdapterName+s);
                    this.add(l);
                }
            }
            this.repaint();
        }
    }

    public class NetworkAdapter {

        public String AdapterName = "";
        public List<String> IPs = new ArrayList<String>();

        public NetworkAdapter(final String Name) {
            this.AdapterName = Name;
        }

        public void addIP(final String IP) {
            this.IPs.add(IP);
        }

        public void print() {
            logger.info("  ADAPTER: {}", this.AdapterName);
            for (final String s : this.IPs) {
                logger.info("    {}", s);
            }
        }
    }

    final public static Logger logger = LoggerFactory.getLogger(Main.class);

    public List<String> SysIPs = new ArrayList<String>();

    public List<NetworkAdapter> Adapters = new ArrayList<NetworkAdapter>();

    public IPFetch() {
        try {
            logger.info("Taking Inventory of Local Network Adapters");
            int cntI = 0;
            for (final Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                final NetworkInterface intf = en.nextElement();
                //final NetworkAdapter n = new NetworkAdapter(intf.getDisplayName());
                final NetworkAdapter n = new NetworkAdapter(intf.getName());
                for (final Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    final InetAddress adde = enumIpAddr.nextElement();
                    if (adde instanceof Inet4Address && !adde.isLoopbackAddress()) {
                        n.addIP(adde.toString());
                        cntI++;
                    }
                }
                // If the adapter isn't loopback and it has at least 1 IP
                // address.
                if (n.IPs.size() > 0) {
                    this.Adapters.add(n);
                }
            }
            logger.info("Found {} IPv4 Addresses connected to {} Adapters", cntI, this.Adapters.size());
            logger.info("IPv6 Addresses and Adapters with no IPv4 Addresses Ignored.");
        } catch (final SocketException e) {
            logger.info(" (error retrieving network interface list)");
        }
    }

    public InfoPanel fetchInfoPanel() {
        final InfoPanel p = new InfoPanel(this.Adapters);
        return p;
    }

    public void print() {
        logger.info("PRINTING NETWORK INFORMATION");
        for (final NetworkAdapter n : this.Adapters) {
            n.print();
        }
    }
}
