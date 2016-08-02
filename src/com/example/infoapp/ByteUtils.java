package com.example.infoapp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteUtils {
	private static ByteUtils instance = null;

	public static ByteUtils getInstance() {
		if (instance == null) {
			instance = new ByteUtils();
		}
		return instance;
	}

	public byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(obj);
		return byteArrayOutputStream.toByteArray();
	}

	public Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		return objectInputStream.readObject();
	}
}
