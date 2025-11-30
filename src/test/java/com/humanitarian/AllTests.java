package com.humanitarian;

import com.humanitarian.dao.*;
import com.humanitarian.util.PasswordUtilTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test suite to run all tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    PasswordUtilTest.class,
    UserDAOTest.class,
    HelpRequestDAOTest.class,
    DocumentDAOTest.class,
    DonationDAOTest.class,
    NotificationDAOTest.class
})
public class AllTests {
    // This class remains empty, used only as a holder for the above annotations
}

