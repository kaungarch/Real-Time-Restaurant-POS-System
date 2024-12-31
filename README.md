# Real-time-Restaurant-POS-Spring-Boot
Real time Restaurant POS with spring boot.

# Restaurant POS System

A real-time Point of Sale (POS) system designed for restaurant environments. This system supports role-based access control, enabling different users (staff) to interact with the system according to their assigned roles. The application uses **Pusher** for real-time communication to provide immediate updates on orders and status changes.

## Features

- **Real-time Updates**: Uses Pusher to provide instant updates to staff members about order status changes.
- **Role-Based Access**: Different user roles (e.g., cooks, waiters, managers) with specific permissions and access to different system functionalities.
- **Order Management**: Staff can view and update order statuses in real-time.
- **No Billing**: The system focuses on order management without handling payment processing or billing.

## Tech Stack

- **Backend**: Spring Boot
- **Frontend**: Next js (TS) (not in this repo)
- **Real-time Communication**: Pusher
- **Database**: MySQL

## Installation

### Prerequisites

- Java 17 or higher
- MySQL or other compatible database
- Node.js and npm (for frontend development)
