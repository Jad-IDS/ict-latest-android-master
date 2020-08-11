package org.shipp.util;

import java.util.ArrayList;
import java.util.List;

import org.shipp.model.Menu;

import com.ids.ict.R;

/**
 * Class to control of menu, add menu to array for add a new menu
 * @author Leonardo Salles
 */
public class MenuConfigAdapter {
	public static List<Menu> getMenuDefault(){
		//Menu menu = new Menu(int id, int drawableIcon, String title, String description)
		Menu menu1 = new Menu(1, R.drawable.about_us, "User", "Username of user");
		Menu menu2 = new Menu(2, R.drawable.about_us, "Register", "Register fields");
		Menu menu3 = new Menu(3, R.drawable.about_us, "My coupons", "Coupons of my app");
		Menu menu4 = new Menu(4, R.drawable.about_us, "Home", "Initial page of my app");
		Menu menu5 = new Menu(5, R.drawable.about_us, "Principal offer", "Principals offers of my app");
		Menu menu6 = new Menu(6, R.drawable.about_us, "Gastronomy", "Offers of gastronomy");
		Menu menu7 = new Menu(7, R.drawable.about_us, "Beauty", "Offers of beauty");
		Menu menu8 = new Menu(8, R.drawable.about_us, "Several", "Several offers");
		Menu menu9 = new Menu(9, R.drawable.about_us, "Products", "Products");

		List<Menu> listMenu = new ArrayList<Menu>();
		listMenu.add(menu1);
		listMenu.add(menu2);
		listMenu.add(menu3);
		listMenu.add(menu4);
		listMenu.add(menu5);
		listMenu.add(menu6);
		listMenu.add(menu7);
		listMenu.add(menu8);
		listMenu.add(menu9);

		return listMenu;
	}
}
