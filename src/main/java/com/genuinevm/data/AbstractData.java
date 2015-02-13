package com.genuinevm.data;

import com.genuinevm.data.collection.DataByteArray;
import com.genuinevm.data.collection.DataCompound;
import com.genuinevm.data.collection.DataArray;
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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

public abstract class AbstractData<T> implements Data<T>, JsonSerializer<AbstractData<T>>, JsonDeserializer<AbstractData<T>> {

	public final byte storageType;

	public AbstractData(byte type) {
		this.storageType = type;
	}

	public static final AbstractData[] TYPES = new AbstractData[] {
			//
			DataNull.INSTANCE, new DataByte(), new DataShort(), new DataInteger(),
			//
			new DataLong(), new DataFloat(), new DataDouble(), new DataByteArray(),
			//
			new DataString(), new DataArray(), new DataCompound(), new DataIntegerArray(),
			//
			DataBoolean.TRUE };

	public static AbstractData create(final byte type) {
		if (type > 0 && type < TYPES.length)
			return (AbstractData) TYPES[type].copy();
		return DataNull.INSTANCE;
	}
}
