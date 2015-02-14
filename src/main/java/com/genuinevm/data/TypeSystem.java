package com.genuinevm.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.genuinevm.data.collection.DataArray;
import com.genuinevm.data.collection.DataByteArray;
import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.collection.DataIntegerArray;
import com.genuinevm.data.primitive.DataBoolean;
import com.genuinevm.data.primitive.DataByte;
import com.genuinevm.data.primitive.DataDouble;
import com.genuinevm.data.primitive.DataFloat;
import com.genuinevm.data.primitive.DataInteger;
import com.genuinevm.data.primitive.DataLong;
import com.genuinevm.data.primitive.DataNull;
import com.genuinevm.data.primitive.DataShort;
import com.genuinevm.data.primitive.DataString;

public class TypeSystem {

	private static TypeSystem instance;

	public static TypeSystem getTypeSystem() {
		if (instance == null)
			instance = new TypeSystem();
		return instance;
	}

	private final Map<Byte, Data> dataTypes = new HashMap<Byte, Data>();

	private TypeSystem() {}

	public Map<Byte, Data> getDataTypes() {
		return Collections.unmodifiableMap(dataTypes);
	}

	public void registerDataType(final byte type, final Data data) throws DataException {
		if (dataTypes.containsKey(data.code()))
			throw new DataException("Tried to register a data type with the same code as one already registered.", data);
		dataTypes.put(type, data);
	}

	public Data createByCode(final byte type) {
		if (dataTypes.containsKey(type))
			dataTypes.get(type);
		return DataNull.INSTANCE;
	}

	private void init() {
		try {
			registerDataType((byte) 0, DataNull.INSTANCE);
			registerDataType((byte) 1, new DataByte());
			registerDataType((byte) 2, new DataShort());
			registerDataType((byte) 3, new DataInteger());
			registerDataType((byte) 4, new DataLong());
			registerDataType((byte) 5, new DataFloat());
			registerDataType((byte) 6, new DataDouble());
			registerDataType((byte) 7, new DataByteArray());
			registerDataType((byte) 8, new DataString());
			registerDataType((byte) 9, new DataArray());
			registerDataType((byte) 10, new DataCompound());
			registerDataType((byte) 11, new DataIntegerArray());
			registerDataType((byte) 12, DataBoolean.FALSE);
		}
		catch (final DataException e) {}
	}
}
