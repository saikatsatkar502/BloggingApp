import React, { useEffect, useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { toast } from 'react-toastify'
import { login } from '../services/AuthService';
import "./Style/Error.css"
import "./Style/Button.css"
import { Card, CardBody, CardFooter, CardHeader, Col, Container, Form, FormFeedback, FormGroup, Input, Label, Row } from 'reactstrap'
import Base from '../components/Base'
import { doLogin } from '../Auth/AuthLogin';

export default function Login() {
    const navigate = useNavigate();
    const [data, setData] = useState({
        email: "",
        password: "",
    })

    const [error, setError] = useState({
        error: {},
        isError: false
    })

    useEffect(() => {
        if (error.isError) {
            toast.error(error.error.message, {
                position: "top-center",
                autoClose: 5000,
                hideProgressBar: false,
                closeOnClick: true,
            }
            )
        }
    }, [error])

    //get item from session storage using useEffect
    useEffect(() => {
        const user = JSON.parse(sessionStorage.getItem("email"));
        if (user) {
            setData({ ...data, email: user })
        }
    }, [])



    const handleChange = (e) => {
        setData({ ...data, [e.target.name]: e.target.value });
    }

    const reset = () => {
        setData({
            email: "",
            password: "",
        })
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        login(data).then((res) => {
            toast.success("Login Successfull with email " + data.email, {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
            })
            setTimeout(() => {
                doLogin(res, () => {
                    reset();
                    navigate("/user/dashboard");
                    sessionStorage.removeItem("email");
                });

            }, 3000)
        }).catch((err) => {
            setError({ error: err.response.data.error, isError: true })
        })

    }


    return (
        <Base>
            <Container >
                <Row>
                    <Col className='col-md-6 offset-md-3 text-capitalize'>
                        <Card className=" Login-Card bg-dark text-light bg-opacity-75">
                            <CardHeader>
                                <h3>LOGIN</h3>
                            </CardHeader>
                            <CardBody className='text-dark'>
                                <Form onSubmit={handleSubmit}>
                                    <FormGroup floating >
                                        <Input
                                            type="email"
                                            name="email"
                                            id="email"
                                            placeholder="Enter Email"
                                            value={data.email}
                                            onChange={handleChange}
                                            invalid={error.isError && error.error.email ? true : false}
                                        />

                                        <Label for="email">Email</Label>
                                        <FormFeedback className='error'>{error.isError && error.error.email ? error.error.email : ""}</FormFeedback>
                                    </FormGroup>

                                    <FormGroup floating  >

                                        <Input
                                            type="password"
                                            name="password"
                                            id="password"
                                            placeholder="Enter Password"
                                            value={data.password}
                                            onChange={handleChange}
                                            invalid={data.password.match(/^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$/) || (error.isError && error.error.password) ? true : false}
                                        />

                                        <Label for="password">Password</Label>
                                        <FormFeedback className='error'>{error.isError && error.error.password ? error.error.password : ""}</FormFeedback>
                                    </FormGroup>

                                    <FormGroup className='my-5'  >
                                        <Input type='submit' placeholder='Login' value={"Login"} className="btn btn-outline-primary register" />
                                    </FormGroup>
                                </Form>

                            </CardBody>

                            <CardFooter>
                                <p>Don't have an account? <NavLink to="/signup" >Register</NavLink></p>
                            </CardFooter>

                        </Card>
                    </Col>
                </Row>

            </Container>
        </Base>
    )
}
