import React, { useEffect, useState } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import { Card, CardBody, CardFooter, CardHeader, Col, Container, Form, FormFeedback, FormGroup, Input, Label, Row } from 'reactstrap'
import { toast } from 'react-toastify'
import "./Style/Error.css"
import "./Style/Button.css"
import Base from '../components/Base'
import { signup } from '../services/AuthService'

export default function Signup() {

    const navigate = useNavigate();

    const [data, setData] = useState({
        name: "",
        email: "",
        password: "",
    })

    const [error, setError] = useState({
        error: {},
        isError: false
    })

    useEffect(() => {
        sessionStorage.removeItem("user");
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

    const handleChange = (e) => {
        setData({ ...data, [e.target.name]: e.target.value });
    }

    const reset = () => {
        setData({
            name: "",
            email: "",
            password: "",
        })
    }

    const handleSubmit = (e) => {
        e.preventDefault();
        signup(data).then((res) => {
            sessionStorage.setItem("email", JSON.stringify(res.email));
            toast.success("Signup Successfull with email " + res.email, {
                position: "top-center",
                autoClose: 2000,
                hideProgressBar: false,
                closeOnClick: true,
            });
            setTimeout(() => {
                reset();
                navigate('/login');
            }, 3000);

        }).catch((err) => {
            setError({ error: err.response.data.error, isError: true })
        })

    }





    return (
        <Base>
            <Container>
                <Row className=''>
                    <Col sm={{ size: 6, offset: 3 }} >
                        <Card color='dark' className='text-dark bg-opacity-75 text-capitalize' inverse>
                            <CardHeader tag={'h3'}>
                                Fill Information to Register
                            </CardHeader>
                            <CardBody className='text-dark text-capitalize'>
                                <Form onSubmit={handleSubmit}>
                                    <FormGroup floating >
                                        <Input
                                            type='text'
                                            id='name'
                                            name='name'
                                            placeholder='Enter Name'
                                            value={data.name}
                                            onChange={handleChange}
                                            invalid={error.isError && error.error.name ? true : false}
                                        />
                                        <Label for='name'>Name</Label>
                                        <FormFeedback className='error'>{error.isError && error.error.name ? error.error.name : ""}</FormFeedback>
                                    </FormGroup>
                                    <FormGroup floating >
                                        <Input
                                            type='email'
                                            id='email'
                                            name='email'
                                            placeholder='Enter Email'
                                            value={data.email}
                                            onChange={handleChange}
                                            invalid={error.isError && error.error.email ? true : false}
                                        />
                                        <Label >Email</Label>
                                        <FormFeedback className='error'>{error.isError && error.error.email ? error.error.email : ""}</FormFeedback>
                                    </FormGroup>
                                    <FormGroup floating >
                                        <Input
                                            type='password'
                                            id='password'
                                            name='password'
                                            placeholder='Enter password'
                                            value={data.password}
                                            onChange={handleChange}
                                            invalid={error.isError && error.error.password ? true : false}
                                        />
                                        <Label >Password</Label>
                                        <FormFeedback className='error'>{error.isError && error.error.password ? error.error.password : ""}</FormFeedback>
                                    </FormGroup>
                                    <FormGroup style={{
                                        display: 'flex',
                                        justifyContent: 'space-between'

                                    }}>
                                        <Input type='submit' value='Register' className='btn btn-outline-primary register ' style={{ marginRight: "0.3rem" }} />
                                        <Input type='button' value='Reset' className='btn btn-outline-light' onClick={() => reset()} />
                                    </FormGroup>
                                </Form>
                            </CardBody>
                            <CardFooter>
                                <p>Already have an account? <NavLink to="/login">Login</NavLink></p>
                            </CardFooter>
                        </Card>
                    </Col>
                </Row>
            </Container>

        </Base>
    )
}
