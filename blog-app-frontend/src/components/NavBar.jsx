import React from 'react'
import { Container, Nav, Navbar, NavDropdown } from 'react-bootstrap'
import { NavLink } from 'react-router-dom'

export default function NavBar() {
    return (
        <Navbar collapseOnSelect expand="lg" bg="dark" variant="dark">
            <Container>
                <NavLink className="navbar-brand" to="/">Blog-App</NavLink>
                <Navbar.Toggle aria-controls="responsive-navbar-nav" />
                <Navbar.Collapse id="responsive-navbar-nav">
                    <Nav className="me-auto">
                        <NavLink className="nav-link" to="/about">About</NavLink>
                        <NavLink className="nav-link" to="/about"></NavLink>
                        <NavDropdown title="More" id="collasible-nav-dropdown">
                            <NavLink className="dropdown-item" to="/contact">Contact Us</NavLink>
                            <NavLink className="dropdown-item" to="/www.linked.in">Linked in</NavLink>
                            <NavDropdown.Divider />
                            <NavLink className="dropdown-item" to="/www.youtube.com">Youyube</NavLink>
                        </NavDropdown>
                    </Nav>
                    <Nav>
                        <NavLink className="nav-link" to="/login">Login</NavLink>
                        <NavLink className="nav-link" to="/signup">
                            SignUp
                        </NavLink>
                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    )
}
