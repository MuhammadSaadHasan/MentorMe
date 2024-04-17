import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.i210566.DataManager
import com.example.i210566.HomePageActivity
import com.example.i210566.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomePageActivityTest {

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(HomePageActivity::class.java)

    @Test
    fun testBottomNavigationViewNavigation() {
        onView(withId(R.id.nav_search)).perform(ViewActions.click())
        // Assuming LetsFindActivity is launched, you might want to check for a view that is unique to that activity.
        // Then navigate back to HomePageActivity.
        pressBack()

        onView(withId(R.id.nav_plus)).perform(ViewActions.click())
        // Assuming AddNewMentor is launched, you might want to check for a view that is unique to that activity.
        pressBack()

        onView(withId(R.id.nav_chat)).perform(ViewActions.click())
        // Assuming Chats is launched, you might want to check for a view that is unique to that activity.
        pressBack()

        onView(withId(R.id.nav_profile)).perform(ViewActions.click())
        // Assuming UserProfile is launched, you might want to check for a view that is unique to that activity.
        pressBack()
    }

    @Test
    fun testDisplayCurrentUserName() {
        // This BookSessionTest assumes that DataManager.currentUser is set before the BookSessionTest runs.
        // You would need to use a mock framework to set this up.
        onView(withId(R.id.HelloUserName)).check(matches(withText(DataManager.currentUser?.name ?: "User")))
    }
}
