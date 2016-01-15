package ninja.genuine.conjure;

import ninja.genuine.conjure.data.Data;
import ninja.genuine.conjure.data.DataException;
import ninja.genuine.conjure.data.collection.DataArray;
import ninja.genuine.conjure.data.collection.DataByteArray;
import ninja.genuine.conjure.data.collection.DataCompound;
import ninja.genuine.conjure.data.collection.DataIntegerArray;
import ninja.genuine.conjure.data.collection.DataList;
import ninja.genuine.conjure.data.primitive.DataBoolean;
import ninja.genuine.conjure.data.primitive.DataByte;
import ninja.genuine.conjure.data.primitive.DataDouble;
import ninja.genuine.conjure.data.primitive.DataFloat;
import ninja.genuine.conjure.data.primitive.DataInteger;
import ninja.genuine.conjure.data.primitive.DataLong;
import ninja.genuine.conjure.data.primitive.DataNull;
import ninja.genuine.conjure.data.primitive.DataShort;
import ninja.genuine.conjure.data.primitive.DataString;

public class TypeSystem {

	private static TypeSystem instance;

	public static TypeSystem getTypeSystem() {
		if (TypeSystem.instance == null)
			TypeSystem.instance = new TypeSystem();
		return TypeSystem.instance;
	}

	private final Data<?>[] dataTypes = new Data<?>[256];

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
		} catch (final DataException e) {}
	}
}
