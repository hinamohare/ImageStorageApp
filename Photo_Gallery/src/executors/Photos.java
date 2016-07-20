/*Photo(s) CRUD operation
 * 
 * C -> Create (Insert)
 * R -> Read (Select)
 * U -> Update (Update)
 * D -> Delete (Delete)
 * 
 * This class also implements function that converts original image to thumbnail
 * */
package executors;
import java.io.*;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import executors.PhotoInfo;

public class Photos {
	//This function returns the arraylist of photoId
	public static ArrayList<PhotoInfo> GetPhotoIDsByUserId(int userId) {

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		String photoName= null;
		Integer photoID= 0;
		//ArrayList photoIDs = new ArrayList();
		ArrayList<PhotoInfo> photoList = new ArrayList<PhotoInfo>();
		try {
			pst = DatabaseHelper.GetPrepareStatement(DatabaseHelper.SelectPhotoId_PhotoNameByUserId);
			pst.setInt(1, userId);
			rs = pst.executeQuery();
			if (rs.next())
				System.out.println("Image IDs and names retrived from database successfully");

			rs.beforeFirst();
			while (rs.next()) {
				//photoIDs.add( rs.getInt("photoID"));
				photoID = rs.getInt("photoID");
				photoName = rs.getString("photoName");
				photoList.add(new PhotoInfo(photoID, photoName));

			}

		} catch (Exception e) {
			System.out.println("exception in retriving photos in Photos : " + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close conn: " + e.getMessage());
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close pst: " + e.getMessage());
				}
			}
			
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close rs: " + e.getMessage());
				}
			}

		}
		//return photoIDs;
		return photoList;
	}
	
	
	// this function converts the original image to thumbnail and returns its 
	
	public static InputStream OriginalToThumbnailConverter(InputStream originalImage)
	{
		BufferedImage originalBufferedImage = null;
		try {
		    originalBufferedImage = ImageIO.read(originalImage);
		}   
		catch(IOException ioe) {
		    System.out.println("IO exception occurred while trying to read image.");
		    
		}
		int thumbnailWidth = 150;
		 
		int widthToScale, heightToScale;
		if (originalBufferedImage.getWidth() > originalBufferedImage.getHeight()) {
		 
		    heightToScale = (int)(1.1 * thumbnailWidth);
		    widthToScale = (int)((heightToScale * 1.0) / originalBufferedImage.getHeight() 
		                    * originalBufferedImage.getWidth());
		 
		} else {
		    widthToScale = (int)(1.1 * thumbnailWidth);
		    heightToScale = (int)((widthToScale * 1.0) / originalBufferedImage.getWidth() 
		                    * originalBufferedImage.getHeight());
		}
		BufferedImage resizedImage = new BufferedImage(widthToScale, 
			    heightToScale, originalBufferedImage.getType());
			Graphics2D g = resizedImage.createGraphics();
			 
			g.setComposite(AlphaComposite.Src);
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			 
			g.drawImage(originalBufferedImage, 0, 0, widthToScale, heightToScale, null);
			g.dispose();
			int x = (resizedImage.getWidth() - thumbnailWidth) / 2;
			int y = (resizedImage.getHeight() - thumbnailWidth) / 2;
			 
			if (x < 0 || y < 0) {
			    throw new IllegalArgumentException("Width of new thumbnail is bigger than original image");
			}
			BufferedImage thumbnailBufferedImage = resizedImage.getSubimage(x, y, thumbnailWidth, thumbnailWidth);
			
			InputStream thumbnailImageStream = null;
		try{	
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(thumbnailBufferedImage, "jpeg", baos);
			thumbnailImageStream = new ByteArrayInputStream(baos.toByteArray());
			
			if(thumbnailImageStream !=null)
				System.out.println("Successfully converted BufferedImage to InputStream");
			
			}
		
		catch(Exception e)
			{
			
				System.out.println("Exception occured in converting BufferedImage to InputStream "+e.getMessage());
			}	
		return thumbnailImageStream;
	}
	

	//function to delete photo
	
	public static boolean DeletePhoto(int photoId) {
		
		Connection conn= null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean status = false;
		try{
			
			conn = DatabaseHelper.GetDBConnection();
			pst = (PreparedStatement)DatabaseHelper.GetPrepareStatement(DatabaseHelper.DeletePhotoByIdQuery,conn )	;
			pst.setInt(1,photoId);
			int count = pst.executeUpdate();
			if(count>0)
				{System.out.println("Photo with photoID "+photoId+" deleted successfully");	
				status = true;
				}
	
		}
		catch(Exception e)
		{
			System.out.println("Exception occured in delete photo Servlet "+e.getMessage());
		}
		finally {
			
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close conn: " + e.getMessage());
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close pst: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Display image: close rs: " + e.getMessage());
				}
			}

		}
		return status;
	}
	

	
	//function to insert photos
	
	public static int InsertPhoto(String photoname, InputStream inputstream, int userId) {

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		// Count shows the number of rows updated in database
		int Count = 0;
		try {

			pst = DatabaseHelper.GetPrepareStatement(DatabaseHelper.InsertPhotoQuery);
			pst.setString(1, photoname);
			pst.setBlob(2, inputstream);
			pst.setBlob(3, inputstream);
			pst.setInt(4, userId);

			Count = pst.executeUpdate(); // returns the number of rows affected

		}

		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Upload : close conn: " + e.getMessage());
				}
			}
			if (pst != null) {
				try {
					pst.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Upload : close pst: " + e.getMessage());
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					System.out.println("Excetion from Upload : close rs: " + e.getMessage());
				}
			}
		}

		return Count;

	}
	
	
}


