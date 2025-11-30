# Humanitarian Support Platform

A comprehensive web application connecting survivors of conflicts (especially those displaced by the Tigray crisis in Ethiopia) with donors, volunteers, and organizations.

## Features

### Survivor Module
- Register/Login with secure authentication
- Create help requests with detailed information
- Upload legal documents (ID, displacement certificate, humanitarian card)
- Track request status: Pending → Approved → Received
- View all uploaded documents and their verification status

### Helper Module
- Register/Login
- Browse only verified help requests
- Filter requests by help type and location
- Donate money, items, or services
- Receive email notifications when survivors respond
- Track donation history

### Admin Module
- Verify legal documents uploaded by survivors
- Approve/Reject help requests
- Block/Unblock fake or duplicate accounts
- Generate comprehensive reports and statistics
- View all system activity

### Additional Features
- Automatic matching based on location and help type
- Impact dashboard with real-time statistics
- English + Amharic language support (partial)
- Mobile-responsive UI using Bootstrap 5
- Hide completed requests from active view
- Secure data handling (sessions, validation, sanitization)
- Email notifications using JavaMail API

## Tech Stack

- **Frontend**: JSP, HTML, CSS, Bootstrap 5
- **Backend**: Java Servlets (Advanced Java)
- **Database**: PostgreSQL 12+ with JDBC
- **Email**: JavaMail API
- **Server**: Apache Tomcat 9.0+
- **Build Tool**: Maven
- **Session Management**: HttpSession

## Prerequisites

- Java JDK 8 or higher
- Apache Maven 3.6+
- PostgreSQL 12+
- Apache Tomcat 9.0+
- SMTP server for email (Gmail, etc.)

## Installation & Setup

### 1. Database Setup

**Using psql command line:**
```bash
# Create database
createdb humanitarian_support

# Connect and run schema
psql -d humanitarian_support -f database/schema.sql
```

**Or manually using psql:**
```sql
-- Connect to PostgreSQL
psql -U postgres

-- Create database
CREATE DATABASE humanitarian_support;

-- Connect to the database
\c humanitarian_support

-- Run the schema file
\i database/schema.sql
```

**Or using pgAdmin:**
1. Create a new database named `humanitarian_support`
2. Open Query Tool and execute the contents of `database/schema.sql`
3. Update database credentials in `src/main/java/com/humanitarian/util/DBConnection.java`

### 2. Configure Database Connection

Edit `src/main/java/com/humanitarian/util/DBConnection.java`:
```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/humanitarian_support";
private static final String DB_USER = "postgres";
private static final String DB_PASSWORD = "your_password";
```

**Note**: Update the connection URL, username, and password according to your PostgreSQL setup.

### 3. Configure Email Service

Edit `src/main/java/com/humanitarian/util/EmailService.java`:
```java
private static final String SMTP_USER = "your-email@gmail.com";
private static final String SMTP_PASSWORD = "your-app-password";
private static final String FROM_EMAIL = "your-email@gmail.com";
```

**Note**: For Gmail, you need to generate an App Password:
1. Go to Google Account settings
2. Enable 2-Step Verification
3. Generate an App Password for "Mail"

### 4. Build the Project

```bash
mvn clean package
```

This will create a WAR file in the `target/` directory.

### 5. Deploy to Tomcat

1. Copy `target/project1.war` to Tomcat's `webapps/` directory
2. Start Tomcat server
3. Access the application at `http://localhost:8080/project1/`

### 6. Default Admin Account

- **Email**: admin@humanitarian.org
- **Password**: admin123

**Important**: Change the default admin password after first login!

## Project Structure

```
project1/
├── database/
│   └── schema.sql              # Database schema
├── src/
│   └── main/
│       ├── java/
│       │   └── com/humanitarian/
│       │       ├── dao/        # Data Access Objects
│       │       ├── model/      # Model classes
│       │       ├── servlet/    # Servlets
│       │       ├── filter/     # Filters
│       │       └── util/       # Utility classes
│       ├── resources/
│       └── webapp/
│           ├── css/           # Stylesheets
│           ├── js/             # JavaScript
│           ├── includes/       # Shared JSP components
│           ├── admin/         # Admin JSP pages
│           ├── helper/        # Helper JSP pages
│           ├── survivor/      # Survivor JSP pages
│           └── WEB-INF/
│               └── web.xml    # Web configuration
└── pom.xml                     # Maven configuration
```

## Usage

### For Survivors

1. Register as a "Survivor"
2. Create a help request with details
3. Upload required legal documents
4. Wait for admin verification
5. Track request status
6. Receive notifications when helpers respond

### For Helpers

1. Register as a "Helper"
2. Browse verified help requests
3. Filter by location and help type
4. Select a request and make a donation
5. Track donation history
6. Receive email notifications

### For Admins

1. Login with admin credentials
2. Review pending help requests
3. Verify uploaded documents
4. Approve or reject requests
5. Manage users (block/unblock)
6. View reports and statistics

## Security Features

- Password hashing using SHA-256
- Session-based authentication
- Role-based access control
- Input validation and sanitization
- File upload restrictions
- SQL injection prevention (PreparedStatements)

## File Upload

- Maximum file size: 10MB
- Allowed formats: PDF, JPG, JPEG, PNG
- Files are stored in `uploads/documents/` directory
- Ensure proper file permissions on the upload directory

## Language Support

The platform supports:
- English (default)
- Amharic (አማርኛ) - partial implementation

Language preference can be set during registration.

## Troubleshooting

### Database Connection Issues
- Verify PostgreSQL is running: `pg_isready` or `systemctl status postgresql`
- Check database credentials
- Ensure PostgreSQL JDBC driver is in classpath (included in pom.xml)
- Verify database exists: `psql -l` to list databases
- Check PostgreSQL is listening on port 5432: `netstat -an | grep 5432`

### Email Not Sending
- Verify SMTP credentials
- Check firewall settings
- For Gmail, ensure App Password is used (not regular password)

### File Upload Issues
- Check directory permissions
- Verify upload directory exists
- Check file size limits

## Development

### Running in Development Mode

```bash
mvn clean compile
mvn tomcat7:run
```

### Testing

The project includes JUnit for testing. Run tests with:
```bash
mvn test
```

## Contributing

This is a humanitarian support platform. Contributions are welcome to improve:
- Security enhancements
- Additional language support
- Performance optimizations
- UI/UX improvements
- Feature additions

## License

This project is developed for humanitarian purposes.

## Support

For issues or questions, please contact the development team.

## Acknowledgments

Built to support survivors of the Tigray crisis in Ethiopia and other conflict-affected regions.

