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
 * The `ReviewController` servlet manages operations related to product reviews.
 * This includes listing reviews for a product, submitting new reviews,
 * updating existing reviews, deleting reviews, and viewing a single review.
 */
@WebServlet(name = "ReviewController", urlPatterns = {"/ReviewController"})
public class ReviewController extends HttpServlet {

    // Data Access Object for Review operations
    private ReviewDAO rdao = new ReviewDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * This method acts as a central dispatcher for various review-related actions
     * based on the "action" request parameter.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");
        
        // Default action if none is provided
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "listReviewsByProduct":
                // Handles fetching and displaying reviews for a specific product.
                listReviewsByProduct(request, response);
                break;
            case "submitReview":
                // Handles the submission of a new product review.
                submitReview(request, response);
                break;
            case "updateReview":
                // Handles updating an existing product review.
                updateReview(request, response);
                break;
            case "deleteReview":
                // Handles deleting a product review.
                deleteReview(request, response);
                break;
            case "viewReview":
                // Handles viewing the details of a single review.
                viewReview(request, response);
                break;
            default:
                // If an unknown action is received, send a 404 Not Found error.
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Hành động đánh giá không hợp lệ.");
        }
    }

    /**
     * Retrieves and displays a list of reviews for a specific product.
     * The product ID is obtained from the "orderedProductId" request parameter.
     * The list of reviews is set as a request attribute and forwarded to "reviews.jsp".
     *
     * @param request The HttpServletRequest object, expecting "orderedProductId".
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     * @throws NumberFormatException if "orderedProductId" is not a valid integer.
     */
    private void listReviewsByProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("orderedProductId"));
            List<ReviewDTO> reviews = rdao.getByOrderedProductId(productId);
            request.setAttribute("reviews", reviews);
            request.getRequestDispatcher("reviews.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã sản phẩm đã đặt hàng không hợp lệ.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi lấy danh sách đánh giá.");
        }
    }

    /**
     * Handles the submission of a new review.
     * It extracts user ID, ordered product ID, rating, and comment from request parameters,
     * creates a new `ReviewDTO`, and attempts to persist it using the ReviewDAO.
     * On success, it redirects to the product's review list; on failure, it sends an error.
     *
     * @param request The HttpServletRequest object, expecting "userId", "orderedProductId", "rating", and "comment".
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     * @throws NumberFormatException if any numeric parameter is not a valid integer.
     */
    private void submitReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int orderedProductId = Integer.parseInt(request.getParameter("orderedProductId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            ReviewDTO review = new ReviewDTO(0, userId, orderedProductId, rating, comment);
            boolean success = rdao.create(review);

            if (success) {
                response.sendRedirect("MainController?action=listReviewsByProduct&orderedProductId=" + orderedProductId);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể gửi đánh giá.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng dữ liệu đầu vào không hợp lệ cho đánh giá.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi gửi đánh giá.");
        }
    }

    /**
     * Handles the update of an existing review.
     * It extracts the review ID, user ID, ordered product ID, new rating, and new comment
     * from request parameters, creates a `ReviewDTO` with the updated information,
     * and attempts to update it using the ReviewDAO.
     * On success, it redirects to the product's review list; on failure, it sends an error.
     *
     * @param request The HttpServletRequest object, expecting "id", "userId", "orderedProductId", "rating", and "comment".
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     * @throws NumberFormatException if any numeric parameter is not a valid integer.
     */
    private void updateReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            int userId = Integer.parseInt(request.getParameter("userId")); // Assuming userId is passed for validation/ownership check
            int orderedProductId = Integer.parseInt(request.getParameter("orderedProductId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            ReviewDTO review = new ReviewDTO(id, userId, orderedProductId, rating, comment);
            boolean success = rdao.update(review);

            if (success) {
                response.sendRedirect("MainController?action=listReviewsByProduct&orderedProductId=" + orderedProductId);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể cập nhật đánh giá.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Định dạng dữ liệu đầu vào không hợp lệ cho cập nhật đánh giá.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi cập nhật đánh giá.");
        }
    }

    /**
     * Handles the deletion of a review.
     * It extracts the review ID from the "id" request parameter and attempts to delete
     * the review using the ReviewDAO.
     * On success, it redirects to the product's review list; on failure, it sends an error.
     *
     * @param request The HttpServletRequest object, expecting "id" and "orderedProductId".
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     * @throws NumberFormatException if "id" or "orderedProductId" is not a valid integer.
     */
    private void deleteReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            // We need orderedProductId to redirect back to the correct product's review list.
            int orderedProductId = Integer.parseInt(request.getParameter("orderedProductId")); 
            
            boolean success = rdao.delete(id);

            if (success) {
                response.sendRedirect("MainController?action=listReviewsByProduct&orderedProductId=" + orderedProductId);
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể xóa đánh giá.");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đánh giá hoặc mã sản phẩm đã đặt hàng không hợp lệ.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xóa đánh giá.");
        }
    }

    /**
     * Handles viewing the details of a single review.
     * It extracts the review ID from the "id" request parameter, retrieves the `ReviewDTO`
     * using the ReviewDAO, sets it as a request attribute, and forwards to "review_detail.jsp".
     *
     * @param request The HttpServletRequest object, expecting "id".
     * @param response The HttpServletResponse object.
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     * @throws NumberFormatException if "id" is not a valid integer.
     */
    private void viewReview(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            ReviewDTO review = rdao.findById(id);
            
            if (review == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy đánh giá.");
                return;
            }
            
            request.setAttribute("review", review);
            request.getRequestDispatcher("review_detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mã đánh giá không hợp lệ.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xem chi tiết đánh giá.");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * Delegates to the `processRequest` method to handle the request.
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
     * Delegates to the `processRequest` method to handle the request.
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
        return "Controller for handling product review operations.";
    }// </editor-fold>

}