package com.app.todo.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.wp.usermodel.Paragraph;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.todo.dto.TaskRepository;
import com.app.todo.models.Task;
import com.app.todo.repository.TaskSpecifications;

@Service
public class HomeExportService 
{

	@Autowired
	private final TaskRepository taskRepository;
	@Autowired
	private final TaskSpecifications taskSpecifications;
	
	public HomeExportService(TaskRepository taskRepository, TaskSpecifications taskSpecifications) 
	{
		this.taskRepository = taskRepository;
		this.taskSpecifications = taskSpecifications;
	}
	
	public ByteArrayInputStream exportToXLSX(Integer filt_category_cd, String allCompleted, String from_dt, String to_dt) throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try(Workbook workbook = new XSSFWorkbook())
		{
			Sheet sheet = workbook.createSheet("Tasks");
			
			Row headerRow = sheet.createRow(0);
			headerRow.createCell(0).setCellValue("Sr#");
			headerRow.createCell(1).setCellValue("Id");
			headerRow.createCell(2).setCellValue("Title");
			headerRow.createCell(3).setCellValue("Description");
			headerRow.createCell(4).setCellValue("Category");
			headerRow.createCell(5).setCellValue("Status");
			
			List<Task> tasks = taskRepository.findAll();
			
			tasks = taskSpecifications.filterTasks(filt_category_cd, allCompleted,from_dt,to_dt);
			
			for(int i=0; i<tasks.size(); i++)
			{
				Row row = sheet.createRow(i + 1);
				row.createCell(0).setCellValue(i+1);
                row.createCell(1).setCellValue(tasks.get(i).getId());
                row.createCell(2).setCellValue(tasks.get(i).getTitle());
                row.createCell(3).setCellValue(tasks.get(i).getDescription());
                
                String categoryName = (tasks.get(i).getCategory() != null) ? tasks.get(i).getCategory().getName() : "-";
                
                row.createCell(4).setCellValue(categoryName);
                row.createCell(5).setCellValue(tasks.get(i).isCompleted() ? "Completed" : "Pending");
			}
			
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			
			out = new ByteArrayOutputStream();
            workbook.write(out);
		} 
		catch (Exception e) 
		{
			System.out.println(e);
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
	
    public ByteArrayInputStream exportToDOCX() throws Exception
    {
        XWPFDocument document = new XWPFDocument();

        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = title.createRun();
        run.setText("Task Report");
        run.setBold(true);
        run.setFontSize(18);

        List<Task> tasks = taskRepository.findAll();
        
        for (Task task : tasks) 
        {
            XWPFParagraph para = document.createParagraph();
            XWPFRun taskRun = para.createRun();
            
            String categoryName = (task.getCategory() != null) ? task.getCategory().getName() : "-";
            String status = task.isCompleted() ? "Completed" : "Pending";
            
            taskRun.setText("• " + task.getTitle() +"( "+categoryName+" )" +" [" + status + "] : "+task.getDescription());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        document.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
    
//    public ByteArrayInputStream exportToPDF() {
//        // Create a new document (PDF)
//    	
//    	Rectangle pageSize = new Rectangle(595, 842);
//		pageSize.setBackgroundColor(BaseColor.WHITE);
//		Document document = new Document(pageSize);
//
//        // Create a ByteArrayOutputStream to hold the generated PDF
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        try {
//            // Create a PdfWriter instance to write the document to the output stream
//            //PdfWriter.getInstance(document, out);
//
//            // Open the document to begin writing content
//            document.open();
//			document.setPageSize(pageSize);
//            document.newPage();
//
//            // Add title to the document
//            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
//            Paragraph title = new Paragraph("Task Report", titleFont);
//            title.setAlignment(Paragraph.ALIGN_CENTER);  // Center the title
//            document.add(title);  // Add the title to the document
//
//            // Add space between title and tasks list
//            document.add(new Paragraph("\n"));
//
//            // Retrieve tasks from the repository
//            List<Task> tasks = taskRepository.findAll();  // Replace with actual data retrieval logic
//
//            // Iterate over each task and add it to the document
//            for (Task task : tasks) {
//                String categoryName = (task.getCategory() != null) ? task.getCategory().getName() : "-";
//                String status = task.isCompleted() ? "Completed" : "Pending";
//
//                // Create task description text
//                String taskText = "• " + task.getTitle() + " (" + categoryName + ") [" + status + "] : " + task.getDescription();
//
//                // Add task text as a paragraph
//                Paragraph taskParagraph = new Paragraph(taskText);
//                document.add(taskParagraph);  // Add task details to the document
//            }
//
//            // Close the document
//            document.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();  // Handle exception (you can log it or throw a runtime exception if needed)
//        }
//
//        // Return the generated PDF as a ByteArrayInputStream
//        return new ByteArrayInputStream(out.toByteArray());
//    }
}
