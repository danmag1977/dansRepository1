package com.dans.domain

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class CDController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond CD.list(params), model:[CDCount: CD.count()]
    }

    def show(CD CD) {
        respond CD
    }

    def create() {
        respond new CD(params)
    }

    @Transactional
    def save(CD CD) {
        if (CD == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (CD.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond CD.errors, view:'create'
            return
        }

        CD.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'CD.label', default: 'CD'), CD.id])
                redirect CD
            }
            '*' { respond CD, [status: CREATED] }
        }
    }

    def edit(CD CD) {
        respond CD
    }

    @Transactional
    def update(CD CD) {
        if (CD == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (CD.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond CD.errors, view:'edit'
            return
        }

        CD.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'CD.label', default: 'CD'), CD.id])
                redirect CD
            }
            '*'{ respond CD, [status: OK] }
        }
    }

    @Transactional
    def delete(CD CD) {

        if (CD == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        CD.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'CD.label', default: 'CD'), CD.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'CD.label', default: 'CD'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
