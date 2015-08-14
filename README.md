# Cornerstone

[![Join the chat at https://gitter.im/kifile/Cornerstone](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/kifile/Cornerstone?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

CornerStone is a useful data manager framework for Android.

Using CornerStone can reduce your work of fetching data and releasing data. 

You can pay more attention on the other thing.

# Introduce

An android application is consist of data and view.

In most cases, we need 

We followed the following principles to make data managing powerful :

1. Using the global manager to manage all data.
2. Using DataProvider to keep data.
3. Using DataFetcher to fetch data.
4. Using DataObserver to watch data change.
5. Manager release the data when not used.


# Requirements

CornerStone supports Android 4.0 (Ice Cream).

**Tips: Exactly, Cornerstone can be used in the platforms below 14 if you edit the build.gradle, but I don't recommend to do that. **

# Using Cornerstone

### 1.Import framework(NOT PUBLISHED YET)

If you are building with gradle, please add the following line to the `dependencies` section of your `build.gradle` file:

	compile 'com.kifile.android.cornerstone:cornerstone:0.1'
	
### 2.Register DataProvider Before Use It

We use the GlobalDataProviderManager to manage all the DataProviders. It means that you shouldn't create a new DataProvider by yourself. 

The right way to get a DataProvider is registering a DataProvider in your GlobalDataProviderManager, then obtaining it from  the method `obtainProvider(KEY)` in GlobalDataProviderManager.

I suggested registering in your Application class like the followings:

	public class App extends Application {
		
		static {
			AbstractDataProviderManager manager = GlobalDataProviderManager().getInstance();
			manager.register(EXAMPLE_KEY, ExampleDataProvider.class);
		}
		
	}

### 3.Use DataProvider

DataProvider is the place where you keep your data.

And we obtain DataProvider 

#### a.For Activity
	
You can see how to use DataProvider [here, Click it](app/src/main/java/com/kifile/android/sample/cornerstone/SampleActivity.java)

#### b.For Fragment

You can see how to use DataProvider [here, Click it](app/src/main/java/com/kifile/android/sample/cornerstone/SampleFragment.java)


# License

Cornerstone is upder [Apache License 2.0](LICENSE)