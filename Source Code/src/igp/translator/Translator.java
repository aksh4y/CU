
	package igp.translator;
	import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

	public class Translator {
	  public static String translate(String text,Language inc, Language out) throws Exception {
	    // Set your Windows Azure Marketplace client info - See http://msdn.microsoft.com/en-us/library/hh454950.aspx
	    Translate.setClientId("IGP-FH-CU-2013");
	    Translate.setClientSecret("BCpwBXPF7F/d/v7R+nfEFmEml5urOtkPFS25Mt7Xez0=");

	    String translatedText = Translate.execute(text, inc, out);
	    return translatedText;

	   
	  }
	  
		public static Language languageChooser(int position)
		{
			switch (position){
			
			case 0:
				
				return Language.GERMAN;
				
			
			case 1:
				return Language.ENGLISH;
				
			case 2:
				return Language.HINDI;
			
			case 3:
				return Language.ITALIAN;
			
			case 4:
				return Language.FRENCH;
				
			default:
			{
				System.out.println("Default has been choosen");
				return Language.ENGLISH;
				
			}
			
			}
		}	
		
	
	}

