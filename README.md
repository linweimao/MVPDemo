
### 1、什么是MVP
MVP，全称 Model-View-Presenter。它是从 MVC中演变过来的，它的基本思想是相通的；在MVP中，**View更加专注于处理数据的可视化以及用户交互，让Model专注于数据的处理**，而Presenter则，提供 **View 与 Model 之间数据的纽带**，用于交互与数据传输；如下面这张图：
 ![image](https://img-blog.csdn.net/20170409164517040)
可以看到，在View 与 Model 之间我们是通过 Presenter，也就是 interface 来实现view 与数据的交互的，大大降低耦合，方便进行单元测试。至于与 MVC 的异同，自行google吧，这里就不细说了。

其实，自己在写代码的时候，心中有个概念就好了，**view 就是UI，model就是数据处理，而persenter 则是他们的纽带**。心中有个轮廓，写起来就不那么费劲了。
### 2、使用 MVP 的 优缺点
我们在使用一种设计模式的时候，首先都会问，为什么要用这种模式，能给我们带来哪些方便？用了这种模式，它的缺点会不会给我的工程造成影响？
首先，优点上，我们上面已经阐述了；

- 减低耦合，实现了 Model 与View 的真正分离，修改 View 而不影响 Model
- 模块职责分明，层次分明，便于维护，多人开发首选。
- Presenter 可以服用，一个 Presenter可以用于多个 View，不用去改 Presenter
- 利于单元测试。模块分明，那么我们编写单元测试就变得很方便了，而不用特别是特别搭平台，人工模拟用户操作等等耗时耗力的事情。

**缺点：**
对于小工程，**额外多出来的代码量，和额外的代码复杂度**，毕竟那么多 interface ，但对于它的有点来说，完全可以接受。

### 3、实战
我们就简单一个数据保存的例子好了。至于数据库的保存，采用郭霖大神的 LitePal，连接如下：
[https://github.com/LitePalFramework/LitePal](https://github.com/LitePalFramework/LitePal)

先上效果：
 ![image](https://img-blog.csdn.net/20170409165058906)
非常简单，就是获取 EditText的数据，保存在数据库，然后重新把它读取出来，结构图如下：
 ![image](https://img-blog.csdn.net/20170409165221409)
首先，从上面的效果图来看，我们需要 name 和 password 这两个字符串，我们需要新建一个 User 类，由于要用到 LitePal ，所以让它继承 DataSupport；如果你使用自己写的，那就不用继承啥了。
```java
public class User extends LitePalSupport {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
```
### Model：
然后，我们思考一下，从View 传过来的数据name和password，我们是要保存起来的，所以，我们先在 model，编写数据保存和读取的方法：
```java
public interface IUserModel {
    void saveUserData(User user);

    User readUserData(String name);
}
```
它的具体实现方法如下:
```java
public class UserModel implements IUserModel {
    private static final String TAG = UserModel.class.getSimpleName();

    public UserModel() {
    }

    /**
     * 使用Litepal保存数据
     *
     * @return
     */
    @Override
    public void saveUserData(User user) {
        user.save();
        Log.d(TAG, "user: " + user);
    }

    /**
     * 通过 name从数据库读取数据
     */
    @Override
    public User readUserData(String name) {
        List<User> userList = LitePal.where("username = ?", name).find(User.class);
        if (userList.size() > 0)
            return userList.get(0);
        return null;
    }
}
```
方法很简单，就保存一下数据和读数据。什么？数据库保存这样就行了？是的，所以赶紧去学习 LitePal 吧。

### View：
View这里，我们是专注于UI的显示和用户交互的，上面我们通过名字的方式从数据库中读取，然后把它显示出来。所以，我们需要添加 name 和password 的显示方法，当然还有出错的方法：
```java
public interface IUserView {
    void setUserName(String userName);
    void setPassword(String password);
    void error(String errormsg);
}
```
然后让mainactivity 继承这个接口重写该方法：
```java
 @Override
    public void setUserName(String userName) {
        mUserEditText.setText(userName);
    }
    @Override
    public void setPassword(String password) {
        mPassEditText.setText(password);
    }
    @Override
    public void error(String errormsg) {
        Toast.makeText(this, errormsg, Toast.LENGTH_SHORT).show();
    }
```
### Presenter ：
好了，现在我们的 View 和 Model 都是单独开了的，所以，我们需要一个纽带，把view 和model 连接起来；那就是我们的 Presenter 了：
```java
public class UserPresenter {

    private static final String TAG = UserPresenter.class.getSimpleName();

    private IUserView mIUserView;
    private IUserModel mIUserModel;

    public UserPresenter(IUserView mIUserView) {

        this.mIUserView = mIUserView;
        mIUserModel = new UserModel();
    }

    /**
     * 数据保存
     *
     * @param user
     */
    public void saveUser(User user) {
        mIUserModel.saveUserData(user);
    }

    /**
     * 读取数据
     *
     * @param name
     */
    public void readUser(String name) {
        User user = mIUserModel.readUserData(name);
        Log.d(TAG, "getread: " + user);
        if (user != null) {
            mIUserView.setUserName(user.getUsername());
            mIUserView.setPassword(user.getPassword());
        } else {
            mIUserView.error("没有找到");
        }
    }
}
```
ok，其他都写好了，那么接下来，只要写onclick事件就可以了：
```java
//保存
    public void save(View view){
        User user = new User();
        user.setUsername(mUserEditText.getText().toString());
        user.setPassword(mPassEditText.getText().toString());
        mUserPresenter.saveUser(user);
    }
    //清空 edittext 
    public void clear(View view){
        mUserEditText.setText("");
        mPassEditText.setText("");
    }
    //读数据
    public void read(View view){
        mUserPresenter.readUser(mReadEditText.getText().toString());
    }
```
这样，我们就实现了一个简单的 MVP demo了。

### 疑问：
1. 这样，你可能会有疑问？一个简单的demo被你写得这么复杂，还说这个设计模式有多好？
2. 这么简单的，接口都那么多，那稍微复杂点的，那接口不是直接上天和太阳肩并肩了吗？

可能很多人有这样的疑问，是的，在一些小工程，或者一个项目只有一两个人开发的时候，你可以酌情考虑要不要使用这种模式，而使用这种模式，就考验你对接口的理解和使用了。
而稍微复杂点的，和多人开发的，我建议是使用这种模式的，当然没有完美的框架，适合自己的才是最重要的，反正我用的挺嗨的，有点搞逼格的感觉，笔者就试过一个稍微复杂的项目，多人开发，最后搞得很混乱，牵一发而动全一身；

