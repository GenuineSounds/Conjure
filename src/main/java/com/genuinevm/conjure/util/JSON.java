package com.genuinevm.conjure.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import com.genuinevm.conjure.collection.DataCompound;
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
			JSON.GSON.toJson(compound, DataCompound.class, wr);
			wr.close();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static String toJSON(final DataCompound compound) {
		return JSON.GSON.toJson(compound, DataCompound.class);
	}

	public static DataCompound loadFromJSON(final File file) {
		DataCompound out = null;
		if (!file.exists())
			return out;
		try {
			final FileReader fr = new FileReader(file);
			out = JSON.GSON.fromJson(fr, DataCompound.class);
			fr.close();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public static DataCompound thereAndBack(final DataCompound compound) {
		return JSON.GSON.fromJson(JSON.GSON.toJson(compound), DataCompound.class);
	}

	static {
		JSON.GSON_BUILDER.setPrettyPrinting();
		JSON.GSON_BUILDER.enableComplexMapKeySerialization();
		// This is the only de/serializer needed since this is the only enclosing type that is serialized directly.
		JSON.GSON_BUILDER.registerTypeAdapter(DataCompound.class, new DataCompound());
		GSON = JSON.GSON_BUILDER.create();
	}
}
