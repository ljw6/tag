package snnu.ljw.tags.dao;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import snnu.ljw.tags.Tag;

public class TagDAO
{
	private Activity context;
	private OkHttpClient client = new OkHttpClient();
	private Gson gson = new Gson();

	public TagDAO(Activity context)
	{
		this.context = context;
	}

	// 获取所有Tag
	public void getAllTags(ApiUse<List<Tag>> apiUse)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				try {
					Request request = new Request.Builder()
							.url(ApiConfig.BASE_URL + "/tagapi/tags")
							.get()
							.build();
					Response response = client.newCall(request).execute();
					String responseJson = response.body().string();
					List<Tag> tagList = gson.fromJson(responseJson, new TypeToken<List<Tag>>(){}.getType());
					System.out.println("list="+tagList.get(1).getName());
					if (tagList == null)
					{
						throw new RuntimeException("查询返回结果为null");
					}
					context.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							apiUse.onSuccess(tagList);
						}
					});
				}
				catch (Exception e) {
					context.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							apiUse.onFail(e);
						}
					});
				}
			}
		}).start();
	}

	// 获取一个Tag
	public void getAnTag(String tagName, ApiUse<Tag> apiUse)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				try {
					Request request = new Request.Builder()
							.url(ApiConfig.BASE_URL + "/tagapi/tag/" + tagName)
							.get()
							.build();
					Response response = client.newCall(request).execute();
					String responseJson = response.body().string();
					Tag tag = gson.fromJson(responseJson, Tag.class);
					if (tag == null || tag.getName() == null)
					{
						throw new RuntimeException("查询返回结果为null");
					}
					context.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							apiUse.onSuccess(tag);
						}
					});
				}
				catch (Exception e) {
					context.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							apiUse.onFail(e);
						}
					});
				}
			}
		}).start();
	}

	// 增加一个Tag
	public void addAnTag(Tag tag, ApiUse<Tag> apiUse)
	{
		new Thread(new Runnable() {
			@Override
			public void run()
			{
				try {
					RequestBody requestBody = RequestBody.create(gson.toJson(tag), ApiConfig.JSON);
					Request request = new Request.Builder()
							.url(ApiConfig.BASE_URL + "/tagapi/add")
							.post(requestBody)
							.build();
					Response response = client.newCall(request).execute();
					String responseJson = response.body().string();
					Tag tag1 = gson.fromJson(responseJson, Tag.class);
					if (tag1 == null || tag1.getName() == null)
					{
						throw new RuntimeException("增加Tag失败");
					}
					context.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							apiUse.onSuccess(tag1);
						}
					});
				}
				catch (Exception e) {
					context.runOnUiThread(new Runnable() {
						@Override
						public void run()
						{
							apiUse.onFail(e);
						}
					});
				}
			}
		}).start();
	}
}
