# PostgreSQL Setup Guide

## Quick Setup

### 1. Install PostgreSQL
Download and install PostgreSQL from https://www.postgresql.org/download/

### 2. Create Database

```bash
# Using psql command line
createdb humanitarian_support

# Or using psql interactive
psql -U postgres
CREATE DATABASE humanitarian_support;
\q
```

### 3. Run Schema

```bash
# Using psql
psql -U postgres -d humanitarian_support -f schema.sql

# Or connect first, then run
psql -U postgres -d humanitarian_support
\i schema.sql
```

### 4. Verify Installation

```bash
psql -U postgres -d humanitarian_support
\dt  # List all tables
SELECT * FROM users;  # Should show admin user
\q
```

## Default Admin Credentials

- **Email**: admin@humanitarian.org
- **Password**: admin123

**Note**: The password is stored as SHA-256 hash in the database. The hash value is:
`240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a`

## Connection Configuration

Update `src/main/java/com/humanitarian/util/DBConnection.java`:

```java
private static final String DB_URL = "jdbc:postgresql://localhost:5432/humanitarian_support";
private static final String DB_USER = "postgres";  // Your PostgreSQL username
private static final String DB_PASSWORD = "postgres";  // Your PostgreSQL password
```

## Troubleshooting

### Connection Refused
- Ensure PostgreSQL is running: `pg_isready` or `systemctl status postgresql` (Linux)
- Check if PostgreSQL is listening on port 5432
- Verify firewall settings

### Authentication Failed
- Check username and password in DBConnection.java
- Verify pg_hba.conf allows local connections
- Try using `localhost` instead of `127.0.0.1`

### Database Does Not Exist
- Create the database: `createdb humanitarian_support`
- Or use: `psql -U postgres -c "CREATE DATABASE humanitarian_support;"`

### Permission Denied
- Ensure your PostgreSQL user has CREATE privileges
- You may need to run as postgres superuser: `sudo -u postgres psql`

## PostgreSQL vs MySQL Differences

The schema has been converted from MySQL to PostgreSQL:

1. **AUTO_INCREMENT** → **SERIAL** (auto-incrementing integers)
2. **ENUM** → **VARCHAR with CHECK constraints** (PostgreSQL doesn't support ENUM the same way)
3. **ENGINE=InnoDB** → Removed (not needed in PostgreSQL)
4. **CHARSET** → Removed (PostgreSQL uses UTF-8 by default)
5. **ON UPDATE CURRENT_TIMESTAMP** → **Trigger function** (PostgreSQL uses triggers for auto-update)

## Testing Connection

You can test the connection using a simple Java program or by running the application and checking logs.

