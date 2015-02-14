package com.genuinevm.data.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map.Entry;

import com.genuinevm.data.Data;
import com.genuinevm.data.TypeSystem;
import com.genuinevm.data.collection.DataCompound;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSON {

	private static final Gson GSON;
	private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

	public static void saveDataToJSON(final DataCompound compound, final File file) {
		try {
			if (!file.exists())
				file.createNewFile();
			final Writer wr = new OutputStreamWriter(new FileOutputStream(file), Charset.forName("UTF-8"));
			GSON.toJson(compound, DataCompound.class, wr);
			wr.close();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static DataCompound loadFromJSON(final File file) {
		DataCompound out = null;
		if (!file.exists())
			return out;
		try {
			final FileReader fr = new FileReader(file);
			out = GSON.fromJson(fr, DataCompound.class);
			fr.close();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static DataCompound thereAndBack(final DataCompound compound) {
		return GSON.fromJson(GSON.toJson(compound), DataCompound.class);
	}

	static {
		GSON_BUILDER.setPrettyPrinting();
		GSON_BUILDER.enableComplexMapKeySerialization();
		for (final Entry<Byte, Data> entry : TypeSystem.getTypeSystem().getDataTypes().entrySet())
			GSON_BUILDER.registerTypeAdapter(entry.getValue().getClass(), entry.getValue());
		GSON = GSON_BUILDER.create();
	}
}
