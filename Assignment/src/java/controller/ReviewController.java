package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.dao.ReviewDAO;
import model.dto.ReviewDTO;

/**
 * ReviewController handles all review-related operations, such as listing,
 * submitting, updating, deleting, and viewing reviews.
 */
@WebServlet(name = "ReviewController", urlPatterns = {"/ReviewController"})
public class ReviewController extends HttpServlet {

    private ReviewDAO rdao = new ReviewDAO();

    /**
     * Processes incoming requests and dispatches them based on the "action" parameter.
     *
     * @param request the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "listReviewsByProduct":
                listReviewsByProduct(request, response);
                break;
            case "submitReview":
                submitReview(request, response);
                break;
            case "updateReview":
                updateReview(request, response);
                break;
            case "deleteReview":
                deleteReview(request, response);
                break;
            case "viewReview":
                viewReview(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Retrieves and displays all reviews for a specific ordered product.
     *
     * @param request contains the orderedProductId
     * @param response forwards the review list to reviews.jsp
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void listReviewsByProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("orderedProductId"));
        List<ReviewDTO> reviews = rdao.getByOrderedProductId(productId);
        request.setAttribute("reviews", reviews);
        request.getRequestDispatcher("reviews.jsp").forward(request, response);
    }

    /**
     * Submits a new review for an ordered product.
     *
     * @param request contains userId, orderedProductId, rating, and comment
     * @param response redirects to review list on success or returns error
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void submitReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int orderedProductId = Integer.parseInt(request.getParameter("orderedProductId"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        ReviewDTO review = new ReviewDTO(0, userId, orderedProductId, rating, comment);
        boolean success = rdao.create(review);

        if (success) {
            response.sendRedirect("MainController?action=listReviewsByProduct&orderedProductId=" + orderedProductId);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to submit review.");
        }
    }

    /**
     * Updates an existing review.
     *
     * @param request contains review id and updated data
     * @param response redirects to review list on success or returns error
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void updateReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        int orderedProductId = Integer.parseInt(request.getParameter("orderedProductId"));
        int rating = Integer.parseInt(request.getParameter("rating"));
        String comment = request.getParameter("comment");

        ReviewDTO review = new ReviewDTO(id, userId, orderedProductId, rating, comment);
        boolean success = rdao.update(review);

        if (success) {
            response.sendRedirect("MainController?action=listReviewsByProduct&orderedProductId=" + orderedProductId);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update review.");
        }
    }

    /**
     * Deletes a review based on its ID.
     *
     * @param request contains review id and orderedProductId
     * @param response redirects to review list on success or returns error
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void deleteReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean success = rdao.delete(id);

        if (success) {
            response.sendRedirect("MainController?action=listReviewsByProduct&orderedProductId=" + request.getParameter("orderedProductId"));
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete review.");
        }
    }

    /**
     * Retrieves and displays a single review's detail.
     *
     * @param request contains the review id
     * @param response forwards to review_detail.jsp with the review data
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private void viewReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ReviewDTO review = rdao.findById(id);
        request.setAttribute("review", review);
        request.getRequestDispatcher("review_detail.jsp").forward(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}