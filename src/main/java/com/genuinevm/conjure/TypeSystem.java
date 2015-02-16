package com.genuinevm.conjure;

import com.genuinevm.conjure.collection.DataArray;
import com.genuinevm.conjure.collection.DataByteArray;
import com.genuinevm.conjure.collection.DataCompound;
import com.genuinevm.conjure.collection.DataIntegerArray;
import com.genuinevm.conjure.collection.DataList;
import com.genuinevm.conjure.primitive.DataBoolean;
import com.genuinevm.conjure.primitive.DataByte;
import com.genuinevm.conjure.primitive.DataDouble;
import com.genuinevm.conjure.primitive.DataFloat;
import com.genuinevm.conjure.primitive.DataInteger;
import com.genuinevm.conjure.primitive.DataLong;
import com.genuinevm.conjure.primitive.DataNull;
import com.genuinevm.conjure.primitive.DataShort;
import com.genuinevm.conjure.primitive.DataString;

public class TypeSystem {

	private static TypeSystem instance;

	public static TypeSystem getTypeSystem() {
		if (TypeSystem.instance == null)
			TypeSystem.instance = new TypeSystem();
		return TypeSystem.instance;
	}

	private final Data<?>[] dataTypes = new Data<?>[512];

	private TypeSystem() {
		init();
	}

	public void registerDataType(final byte type, final Data<?> data) throws DataException {
		final int i = type & 0xFF;
		if (dataTypes[i] != null)
			throw new DataException("Tried to register a data type with the same code as one already registered.", data);
		dataTypes[i] = data;
	}

	public Data<?> createByCode(final byte type) {
		final int i = type & 0xFF;
		if (dataTypes[i] != null)
			return dataTypes[i].copy();
		return null;
	}

	private void init() {
		try {
			registerDataType(DataNull.CODE, DataNull.INSTANCE);
			registerDataType(DataByte.CODE, new DataByte());
			registerDataType(DataShort.CODE, new DataShort());
			registerDataType(DataInteger.CODE, new DataInteger());
			registerDataType(DataLong.CODE, new DataLong());
			registerDataType(DataFloat.CODE, new DataFloat());
			registerDataType(DataDouble.CODE, new DataDouble());
			registerDataType(DataByteArray.CODE, new DataByteArray());
			registerDataType(DataString.CODE, new DataString());
			registerDataType(DataArray.CODE, new DataArray());
			registerDataType(DataCompound.CODE, new DataCompound());
			registerDataType(DataIntegerArray.CODE, new DataIntegerArray());
			registerDataType(DataBoolean.CODE, new DataBoolean());
			registerDataType(DataList.CODE, new DataList());
		}
		catch (final DataException e) {}
	}
}
