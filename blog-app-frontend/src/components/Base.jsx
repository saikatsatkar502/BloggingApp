import React from 'react'
import CustomeNavBar from './CustomeNavBar'
import "./style/BaseStyle.css"

export default function Base({ title = "Blog-App", children }) {
    return (
        <div className='container-fluid p-0 m-0 overflow-scroll '>
            <div className='header '>
                <CustomeNavBar dark fixed="top" expand="md" color="dark" className="mb-5" container="md" />
            </div>
            <div className='main'>
                {children}
            </div>
            <div className='bg-opacity-75 footer '>
                <h3>Footer</h3>
            </div>
        </div>
    )
}
