package xmu.swordbearer.sinaplugin.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * SinaUser 新浪用户的数据
 * 
 * @author SwordBearers
 * 
 */
public class SinaUser extends BaseBean {
	private static final long serialVersionUID = 8114538439595607908L;

	private long id;
	private String screen_name;// 用户昵称
	private String name;// 友好显示名称
	//
	private int province;
	private int city;
	private String location;
	private String description;
	//
	private String url;// 博客地址
	private String profile_image_url;// 头像地址
	private String domain;// 个性域名
	private String gender;// 性别
	//
	private int followers_count;// 粉丝数
	private int friends_count;// 关注数
	private int statuses_count;// 微博数
	private int favourites_count;// 收藏数
	//
	private String created_at;// 用户创建时间
	// private boolean following;// false暂未支持
	private boolean verified;// 是否认证用户
	public String remark;// 备注信息

	//
	private boolean allow_all_comment;// 是否允许所有人对我评论
	private String avatar_large;// 大头像
	private boolean follow_me;// false
	private int online_status;// 用户是否在线. 1 在线，0 不在线
	private int bi_followers_count;// 互粉数

	public static SinaUser fromJSON(String jsonStr) throws JSONException {
		JSONObject jsonUser = new JSONObject(jsonStr);
		return new SinaUser(jsonUser);
	}

	public SinaUser(JSONObject json) throws JSONException {
		id = json.getLong("id");
		screen_name = json.getString("screen_name");
		name = json.getString("name");
		//
		province = json.getInt("province");
		city = json.getInt("city");
		location = json.getString("location");
		description = json.getString("description");
		//
		url = json.getString("url");
		profile_image_url = json.getString("profile_image_url");
		domain = json.getString("domain");
		gender = json.getString("gender");
		//
		followers_count = json.getInt("followers_count");
		friends_count = json.getInt("friends_count");
		statuses_count = json.getInt("statuses_count");
		favourites_count = json.getInt("favourites_count");
		//
		created_at = json.getString("created_at");
		// following = json.getBoolean("following");
		verified = json.getBoolean("verified");
		if (json.has("remark")) {
			remark = json.getString("remark");
		} else {
			remark = null;
		}
		//
		allow_all_comment = json.getBoolean("allow_all_comment");
		avatar_large = json.getString("avatar_large");
		follow_me = json.getBoolean("follow_me");
		online_status = json.getInt("online_status");
		bi_followers_count = json.getInt("bi_followers_count");
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowers_count() {
		return followers_count;
	}

	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}

	public int getFriends_count() {
		return friends_count;
	}

	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}

	public int getStatuses_count() {
		return statuses_count;
	}

	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}

	public int getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public boolean isAllow_all_comment() {
		return allow_all_comment;
	}

	public void setAllow_all_comment(boolean allow_all_comment) {
		this.allow_all_comment = allow_all_comment;
	}

	public String getAvatar_large() {
		return avatar_large;
	}

	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}

	public boolean isFollow_me() {
		return follow_me;
	}

	public void setFollow_me(boolean follow_me) {
		this.follow_me = follow_me;
	}

	public int getOnline_status() {
		return online_status;
	}

	public void setOnline_status(int online_status) {
		this.online_status = online_status;
	}

	public int getBi_followers_count() {
		return bi_followers_count;
	}

	public void setBi_followers_count(int bi_followers_count) {
		this.bi_followers_count = bi_followers_count;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
