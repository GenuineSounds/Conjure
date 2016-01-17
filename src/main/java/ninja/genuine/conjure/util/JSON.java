package ninja.genuine.conjure.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ninja.genuine.conjure.data.collection.DataCompound;

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
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static String toJSON(final DataCompound compound) {
		return GSON.toJson(compound, DataCompound.class);
	}

	public static DataCompound loadFromJSON(final File file) {
		DataCompound out = null;
		if (!file.exists())
			return out;
		try {
			final FileReader fr = new FileReader(file);
			out = GSON.fromJson(fr, DataCompound.class);
			fr.close();
		} catch (final Exception e) {
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
		// This is the only de/serializer needed since this is the only enclosing type that is serialized directly.
		GSON_BUILDER.registerTypeAdapter(DataCompound.class, new DataCompound());
		GSON = GSON_BUILDER.create();
	}
}
