import React from 'react'
import { Pagination, PaginationItem, PaginationLink } from 'reactstrap'

export default function PaginationComponent(props) {
    const { currentPage, totalPages, handlePaginationClick } = props;


    const handlePagination = (pageNo) => {
        handlePaginationClick(pageNo);
    }




    return (
        <Pagination size="sm">
            <PaginationItem onClick={() => handlePagination(0)} disabled={currentPage === 0}>
                <PaginationLink
                    first
                />
            </PaginationItem>
            {
                [...Array(totalPages)].map((page, i) => (
                    <PaginationItem onClick={() => handlePagination(i)} active={i === currentPage} key={i}>
                        <PaginationLink >
                            {i + 1}
                        </PaginationLink>
                    </PaginationItem>
                ))

            }


            <PaginationItem onClick={() => handlePagination(totalPages - 1)} disabled={currentPage === totalPages - 1} >
                <PaginationLink
                    last
                />
            </PaginationItem>
        </Pagination>
    )
}
