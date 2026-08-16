# Global Staff Logger

Global Staff Logger is a high-performance logging plugin developed specifically for the Velocity Proxy. The primary goal is to intercept and log player activities across the entire network, allowing staff members to monitor players globally, regardless of which backend server they are currently connected to.

## Core Features

### 1. Command Logging
Log every command entered by a player across all linked servers, including commands that do not exist on the backend server.
- **Log Format:** `{player} ({server}) : {command}`
- **Example:** `TheRiox (Lobby) : /plugins`

### 2. Global Chat Logging
Log all chat messages sent by players in any server within the network.
- **Log Format:** `{player} ({server}) : {message}`
- **Example:** `TheRiox (Practice) : Hello`

### 3. Connection & Movement Logging
Log when a player switches between servers (via command, portal, or plugin) or when they are kicked/moved.
- **Log Format:** `{player} switched from {server} to {server}`
- **Example:** `TheRiox switched from Lobby to Practice`

### 4. Toggleable Spy Mode
Staff members can toggle Spy mode to monitor live activity in real-time across the network.
- `/spy chat` - Toggle global chat messages spy.
- `/spy cmd` - Toggle global command usage spy.
- `/spy sw` - Toggle server switches/movement spy.

## Permissions

Each feature is controlled by a specific permission node:
- `nedayazady.spy.chat` - Access to `/spy chat` and receiving global chat logs.
- `nedayazady.spy.cmd` - Access to `/spy cmd` and receiving global command logs.
- `nedayazady.spy.sw` - Access to `/spy sw` and receiving connection/movement logs.

## Building from source

1. Clone the repository.
2. Run `mvn clean package`.
3. The built jar will be located in the `target/` directory.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
