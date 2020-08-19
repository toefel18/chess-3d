package game;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SaveGame
{
	private Game 	game;
	private String 	filePath;
	
	public SaveGame(Game game, String filePath)
	{
		this.game 		= game;
		this.filePath 	= filePath;
	}
	
	public void save() throws Exception
	{
		saveContent(buildContent());
	}
	
	// TODO	Init a new Game with the correct Player's, before CommandProcessor will execute commands.
	public void load() throws Exception
	{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this.filePath);
		
		XPath xPath =  XPathFactory.newInstance().newXPath();
	    
		NodeList commands = (NodeList) xPath.compile("/savegame/commands/command").evaluate(document, XPathConstants.NODESET);
		
		for(int i = 0, max = commands.getLength(); i < max; i++)
			CommandProcessor.restoreCommand(game, (Element) commands.item(i));
	}
	
	private Document buildContent() throws ParserConfigurationException
	{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		{
			Element saveGameElement = document.createElement("savegame");
			{
				saveGameElement.setAttribute("date", new Date().toString());	
				
				Element commandsElement = document.createElement("commands");
				{
					for(Command command : game.getCommandProcessor().getExecutedCommands())
					{
						Element commandElement = document.createElement("command");
						{
							command.saveState(commandElement);
						}
						commandsElement.appendChild(commandElement);
					}
				}
				saveGameElement.appendChild(commandsElement);
			}
			document.appendChild(saveGameElement);
		}
		
		return document;
	}
	
	private void saveContent(Document document) throws Exception
	{
		File file = new File(filePath);
		
		if(!file.exists())
			file.createNewFile();
		
		FileWriter fileWriter = new FileWriter(filePath);

		TransformerFactory.newInstance().newTransformer().transform(new DOMSource(document), new StreamResult(fileWriter)); 
		
		fileWriter.close();
	}
}
