import { React, useEffect, useState } from 'react'
import { NavLink as ReactNavLink, useNavigate } from 'react-router-dom'
import "./style/CustomeNavStyle.css"
import {
    Collapse,
    Navbar,
    NavbarToggler,
    NavbarBrand,
    Nav,
    NavItem,
    NavLink,
    UncontrolledDropdown,
    DropdownToggle,
    DropdownMenu,
    DropdownItem,
    NavbarText,
} from 'reactstrap';
import { isLogin, doLogout, getCurrentUser } from '../Auth/AuthLogin';
import { toast } from 'react-toastify';

export default function CustomeNavBar(args) {
    const navigate = useNavigate();

    const [isOpen, setIsOpen] = useState(false);
    const toggle = () => setIsOpen(!isOpen);

    const [login, setlogin] = useState(false);
    const [user, setUser] = useState(null);


    const logout = () => {
        doLogout(() => {

            toast.success("Logout Successfull", {
                position: "top-center",
                autoClose: 3000,
                hideProgressBar: false,
                closeOnClick: true,
            })
            setTimeout(() => {
                navigate("/");
                setlogin(false);
            }, 3000);
        })

    }

    useEffect(() => {
        setlogin(isLogin());
        setUser(getCurrentUser());
    }, [login])


    return (
        <div>
            <Navbar {...args}>
                <NavbarBrand tag={ReactNavLink} to="/">reactstrap</NavbarBrand>
                <NavbarToggler onClick={toggle} />
                <Collapse isOpen={isOpen} navbar>
                    <Nav className="me-auto" navbar>
                        <NavItem>
                            <NavLink tag={ReactNavLink} to="/about">About</NavLink>
                        </NavItem>

                        <UncontrolledDropdown right="end" nav inNavbar >
                            <DropdownToggle nav caret >
                                more
                            </DropdownToggle>
                            <DropdownMenu end>
                                <DropdownItem tag={ReactNavLink} to="/services">Services</DropdownItem>
                                <DropdownItem tag={ReactNavLink} to="/contact">Contact Us</DropdownItem>
                                <DropdownItem divider />
                                <DropdownItem>Youtube</DropdownItem>
                            </DropdownMenu>
                        </UncontrolledDropdown>
                    </Nav>
                    <NavbarText className='me-2 version'>Version : 1.0.0
                    </NavbarText>
                    {/* horizontal line */}
                    <hr style={{
                        color: 'white',
                        backgroundColor: 'white',
                        height: 1,
                        borderColor: 'white'

                    }} />

                    <Nav navbar className='pe2 '>

                        {
                            login && user !== undefined ? (
                                <>
                                    <UncontrolledDropdown right="end" nav inNavbar >
                                        <DropdownToggle nav caret >
                                            {user.email}
                                        </DropdownToggle>
                                        <DropdownMenu end>
                                            <DropdownItem tag={ReactNavLink} to="/user/profile">Profile</DropdownItem>
                                            <DropdownItem tag={ReactNavLink} to="/user/dashboard">Dashboard</DropdownItem>
                                            <DropdownItem divider />
                                            <DropdownItem onClick={() => logout()}>Logout</DropdownItem>
                                        </DropdownMenu>
                                    </UncontrolledDropdown>
                                </>
                            ) : (
                                <>
                                    <NavItem>
                                        <NavLink tag={ReactNavLink} to="/login">Login</NavLink>
                                    </NavItem>

                                    <NavItem>
                                        <NavLink tag={ReactNavLink} to="/signup">
                                            Signup
                                        </NavLink>
                                    </NavItem>
                                </>
                            )

                        }


                    </Nav>


                </Collapse>
            </Navbar>
        </div>
    )
}
