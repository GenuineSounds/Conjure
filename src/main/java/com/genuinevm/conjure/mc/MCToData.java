package com.genuinevm.conjure.mc;

import com.genuinevm.conjure.data.Data;
import com.genuinevm.conjure.data.DataException;
import com.genuinevm.conjure.data.collection.DataArray;
import com.genuinevm.conjure.data.collection.DataByteArray;
import com.genuinevm.conjure.data.collection.DataCompound;
import com.genuinevm.conjure.data.collection.DataIntegerArray;
import com.genuinevm.conjure.data.primitive.DataBoolean;
import com.genuinevm.conjure.data.primitive.DataByte;
import com.genuinevm.conjure.data.primitive.DataDouble;
import com.genuinevm.conjure.data.primitive.DataFloat;
import com.genuinevm.conjure.data.primitive.DataInteger;
import com.genuinevm.conjure.data.primitive.DataLong;
import com.genuinevm.conjure.data.primitive.DataNull;
import com.genuinevm.conjure.data.primitive.DataShort;
import com.genuinevm.conjure.data.primitive.DataString;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTBase.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagEnd;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class MCToData {

	public static DataCompound create(final ItemStack stack) {
		return create(stack.writeToNBT(new NBTTagCompound()));
	}

	private static DataNull create(final NBTTagEnd nbt) {
		return DataNull.INSTANCE;
	}

	public static DataByte create(final NBTTagByte nbt) {
		return new DataByte(nbt.getByte());
	}

	public static DataShort create(final NBTTagShort nbt) {
		return new DataShort(nbt.getShort());
	}

	public static DataInteger create(final NBTTagInt nbt) {
		return new DataInteger(nbt.getInt());
	}

	public static DataFloat create(final NBTTagFloat nbt) {
		return new DataFloat(nbt.getFloat());
	}

	public static DataDouble create(final NBTTagDouble nbt) {
		return new DataDouble(nbt.getDouble());
	}

	public static DataLong create(final NBTTagLong nbt) {
		return new DataLong(nbt.getLong());
	}

	public static DataString create(final NBTTagString nbt) {
		return new DataString(nbt.getString());
	}

	public static DataIntegerArray create(final NBTTagIntArray nbt) {
		return new DataIntegerArray(nbt.getIntArray());
	}

	public static DataByteArray create(final NBTTagByteArray nbt) {
		return new DataByteArray(nbt.getByteArray());
	}

	public static DataArray create(final NBTTagList nbt) {
		final DataArray list = new DataArray();
		final int count = nbt.tagCount();
		switch (nbt.getId()) {
		case 6:
			for (int i = 0; i < count; i++)
				list.add(new DataDouble(nbt.getDouble(i)));
			break;
		case 5:
			for (int i = 0; i < count; i++)
				list.add(new DataFloat(nbt.getFloat(i)));
			break;
		case 8:
			for (int i = 0; i < count; i++)
				list.add(new DataString(nbt.getStringTagAt(i)));
			break;
		case 10:
			for (int i = 0; i < count; i++)
				list.add(create(nbt.getCompoundTagAt(i)));
			break;
		case 11:
			for (int i = 0; i < count; i++)
				list.add(new DataIntegerArray(nbt.getIntArray(i)));
			break;
		}
		return list;
	}

	public static DataCompound create(final NBTTagCompound nbt) {
		final DataCompound compound = new DataCompound();
		for (final Object key : nbt.getKeySet())
			try {
				compound.set((String) key, create(nbt.getTag((String) key)));
			}
			catch (final Exception e) {}
		return compound;
	}

	public static DataBoolean create(final NBTPrimitive nbt) {
		return new DataBoolean(nbt.getByte() != 0);
	}

	public static Data<?> create(final NBTBase nbt) throws Exception {
		if (nbt instanceof NBTTagEnd)
			return create((NBTTagEnd) nbt);
		if (nbt instanceof NBTTagByte)
			return create((NBTTagByte) nbt);
		if (nbt instanceof NBTTagShort)
			return create((NBTTagShort) nbt);
		if (nbt instanceof NBTTagInt)
			return create((NBTTagInt) nbt);
		if (nbt instanceof NBTTagFloat)
			return create((NBTTagFloat) nbt);
		if (nbt instanceof NBTTagDouble)
			return create((NBTTagDouble) nbt);
		if (nbt instanceof NBTTagLong)
			return create((NBTTagLong) nbt);
		if (nbt instanceof NBTTagString)
			return create((NBTTagString) nbt);
		if (nbt instanceof NBTTagByteArray)
			return create((NBTTagByteArray) nbt);
		if (nbt instanceof NBTTagIntArray)
			return create((NBTTagIntArray) nbt);
		if (nbt instanceof NBTTagList)
			return create((NBTTagList) nbt);
		if (nbt instanceof NBTTagCompound)
			return create((NBTTagCompound) nbt);
		throw new DataException("Could not create Data from " + nbt.getClass().getSimpleName(), null);
	}
}
