package pgDev.bukkit.UtilityHats;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * Our usual configuration class.
 * @author PG Dev Team (Devil Boy)
 */
public class UHConfig {
	private Properties properties;
	private final UtilityHats plugin;
	public boolean upToDate = true;
	
	// List of Config Options
	int glowHeadAlert = 6;
	int tntRadius = 5;
	int glowPower = 15;
	Double pistonPunchSpeed = 1d;
	Double pistonJumpSpeed = 1d;
	int pistonOverHeat = 24;
	
	public UHConfig(Properties p, final UtilityHats plugin) {
        properties = p;
        this.plugin = plugin;
        
        // Grab values here.
        glowHeadAlert = getInt("glowHeadAlert", 6);
        tntRadius = getInt("tntRadius", 5);
        glowPower = getInt("glowPower", 15);
        pistonPunchSpeed = getDouble("pistonPunchSpeed", 1d);
        pistonJumpSpeed = getDouble("pistonJumpSpeed", 1d);
        pistonOverHeat = getInt("pistonOverHeat", 24);
        
	}
	
	// Value obtaining functions down below
	public int getInt(String label, int thedefault) {
		String value;
        try {
        	value = getString(label);
        	return Integer.parseInt(value);
        } catch (NoSuchElementException e) {
        	return thedefault;
        }
    }
    
    public double getDouble(String label, Double thedefault) {
        String value;
        try {
        	value = getString(label);
        	return Double.parseDouble(value);
        } catch (NoSuchElementException e) {
        	return thedefault;
        }
    }
    
    public File getFile(String label) throws NoSuchElementException {
        String value = getString(label);
        return new File(value);
    }

    public boolean getBoolean(String label, boolean thedefault) {
    	String values;
        try {
        	values = getString(label);
        	return Boolean.valueOf(values).booleanValue();
        } catch (NoSuchElementException e) {
        	return thedefault;
        }
    }
    
    public Color getColor(String label) {
        String value = getString(label);
        Color color = Color.decode(value);
        return color;
    }
    
    public HashSet<String> getSet(String label, String thedefault) {
        String values;
        try {
        	values = getString(label);
        } catch (NoSuchElementException e) {
        	values = thedefault;
        }
        String[] tokens = values.split(",");
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < tokens.length; i++) {
            set.add(tokens[i].trim().toLowerCase());
        }
        return set;
    }
    
    public LinkedList<String> getList(String label, String thedefault) {
    	String values;
        try {
        	values = getString(label);
        } catch (NoSuchElementException e) {
        	values = thedefault;
        }
        if(!values.equals("")) {
            String[] tokens = values.split(",");
            LinkedList<String> set = new LinkedList<String>();
            for (int i = 0; i < tokens.length; i++) {
                set.add(tokens[i].trim().toLowerCase());
            }
            return set;
        }else {
        	return new LinkedList<String>();
        }
    }
    
    public String getString(String label) throws NoSuchElementException {
        String value = properties.getProperty(label);
        if (value == null) {
        	upToDate = false;
            throw new NoSuchElementException("Config did not contain: " + label);
        }
        return value;
    }
    
    public String getString(String label, String thedefault) {
    	String value;
    	try {
        	value = getString(label);
        } catch (NoSuchElementException e) {
        	value = thedefault;
        }
        return value;
    }
    
    public String linkedListToString(LinkedList<String> list) {
    	if(list.size() > 0) {
    		String compounded = "";
    		boolean first = true;
        	for (String value : list) {
        		if (first) {
        			compounded = value;
        			first = false;
        		} else {
        			compounded = compounded + "," + value;
        		}
        	}
        	return compounded;
    	}
    	return "";
    }
    
    
    // Config creation method
    public void createConfig() {
    	try{
    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(plugin.pluginConfigLocation)));
    		out.write("#\r\n");
    		out.write("# UtilityHats Configuration\r\n");
    		out.write("#\r\n");
    		out.write("\r\n");
    		out.write("# Glowing Head Monster Alert Interval\r\n");
    		out.write("#	Here you specify the amount of time (in seconds) between each\r\n");
    		out.write("#	check for monsters around a player with glowstone on his head.\r\n");
    		out.write("glowHeadAlert=" + glowHeadAlert + "\r\n");
    		out.write("\r\n");
    		out.write("# TNT Explosion Radius\r\n");
    		out.write("#	This is pretty much the power of the tnt hat explosions.\r\n");
    		out.write("#	Creeper = 3, TNT = 4, Super Creeper = 5\r\n");
    		out.write("tntRadius=" + tntRadius + "\r\n");
    		out.write("\r\n");
    		out.write("# Glow Hat Strength\r\n");
    		out.write("#	You can set the light level of the glowstone on people's\r\n");
    		out.write("#	heads. 15 is known to be the maximum.\r\n");
    		out.write("glowPower=" + glowPower + "\r\n");
    		out.write("\r\n");
    		out.write("# Piston Punch Launch Speed\r\n");
    		out.write("#	The speed at which entities are launched upwards by piston\r\n");
    		out.write("#	hat wearers when they punch mobs/players. (Can have decimals)\r\n");
    		out.write("pistonPunchSpeed=" + pistonPunchSpeed + "\r\n");
    		out.write("\r\n");
    		out.write("# Piston Jump Launch Speed\r\n");
    		out.write("#	The speed at which players wearing pistons jump upwards\r\n");
    		out.write("#	when powered by redstone. (Can have decimals)\r\n");
    		out.write("pistonJumpSpeed=" + pistonJumpSpeed + "\r\n");
    		out.write("\r\n");
    		out.write("# Piston OverHeat Duration\r\n");
    		out.write("#	This is the length of time (in ticks) for which players\r\n");
    		out.write("#	will be set to fire for overheating.\r\n");
    		out.write("pistonOverHeat=" + pistonOverHeat + "\r\n");
    		out.close();
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    }
}
