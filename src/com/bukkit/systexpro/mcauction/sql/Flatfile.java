package com.bukkit.systexpro.mcauction.sql;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.bukkit.systexpro.mcauction.MCAuction;

public class Flatfile {
	private MCAuction plugin;
	public File file;
	public Map<String, String> listItems = new HashMap<String, String>();

	public Flatfile(String file2, MCAuction instance) {
		plugin = instance;
		file = new File(file2);
	}

	public void write(String item, int amount) {
		try {
			BufferedWriter out = new BufferedWriter ( new FileWriter ( file ) );
			out.append(item);
			out.flush();
			out.close();
			plugin.log.log(Level.INFO, "[MCAuction] Saved Item: " + item);
		} catch (IOException e) {
			plugin.log.log(Level.SEVERE, "[MCAuction] Error writing to file.");
			plugin.log.log(Level.SEVERE, "[MCAuction]" + e);
		}
	}
	
	public void read() {
		try {
		    BufferedReader in = new BufferedReader(new FileReader(file));
		    String str = "";
		    while ((str = in.readLine()) != null) {
		        this.listItems.put(str, "10");
		    }
		    System.out.println(this.listItems);
		    in.close();
		} catch (IOException e) {
		}
	}
	
	public void getItem(String s) {
		if(this.listItems.containsKey(s)) {
			System.out.println("Found Item: " + s);
		} else {
			System.out.println("Unknown Item: " + s);
		}
	}
}



