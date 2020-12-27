package snnu.ljw.tags.dao;

public abstract class ApiUse<T>
{
	public abstract void onSuccess(T result);
	public void onFail(Exception e) {}
}
