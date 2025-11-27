package com.crystal;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CrystalExtractorPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CrystalExtractorPlugin.class);
		RuneLite.main(args);
	}
}
