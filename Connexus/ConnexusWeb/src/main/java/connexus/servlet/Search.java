package connexus.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.CharMatcher;

import com.google.gson.Gson;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import connexus.Config;
import connexus.ConnexusContext;
import connexus.model.AutocompleteIndex;
import connexus.model.Media;
import connexus.model.Site;
import connexus.model.Stream;

public class Search extends ConnexusServletBase {

    /**
     *
     */
    private static final long serialVersionUID = -164245092113045712L;
    public static final String uri = "/search";
    public static final String dispatcher = "/WEB-INF/jsp/search.jsp";


    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization

        if (req.getParameter("term") != null) {
            doHandleAutocomplete(cContext, req, resp);
            return;
        } else if (req.getParameter("q") != null) {
            doHandleSearch(cContext, req, resp);
        }

        // Forward to JSP page to display them in a HTML table.
        req.getRequestDispatcher(dispatcher).forward(req, resp);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        ConnexusContext cContext = InitializeContext(req, resp); // Base site context initialization
        if (req.getParameter("rebuild") != null) {
            UpdateAutocomplete.doGenerateAutocompleteIndex();
            alertInfo(req, "Auto-complete index has been rebuilt.");
        } else {
            alertInfo(req, "TODO: Not implemented yet.");
        }
        resp.sendRedirect(uri);
    }

    public void doHandleAutocomplete(ConnexusContext cContext, HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String term = req.getParameter("term");
        final int maxResults = 20;

        AutocompleteIndex autocompleteIndex = AutocompleteIndex.load(null, null);
        List<String> resultList = autocompleteIndex.doSearch(term, maxResults);

        // MAX 20 results
        // case-insensitive substring search
        // Sort results alphabetically

        Gson gson = new Gson();
        resp.setContentType("text/json");
        PrintWriter outpw = resp.getWriter();
        String outJson = gson.toJson(resultList);
        outpw.println(outJson);

    }

    public void doHandleSearch(ConnexusContext cContext, HttpServletRequest req, HttpServletResponse resp) {
        Ref<Site> site = cContext.getSite();
        String queryTerm = req.getParameter("q");
        queryTerm = CharMatcher.WHITESPACE.trimFrom(queryTerm);
        // System.err.println("User " + cuser + " searched for " + queryTerm);
        if (queryTerm == null || queryTerm.length() < 1) {
            alertInfo(req, "Please type a search term.");
            return;
        }

        // Has searched for something, ok to show results pane
        req.setAttribute("showSearchResults", true);
        req.setAttribute("q", queryTerm);

        // random hardcoded limit for web since no paging
        List<Stream> searchResults = performStreamSearch(queryTerm, site.getKey(), 40, 0);
        req.setAttribute("searchResultsList", searchResults);

    }


    public static List<Stream> performStreamSearch(String queryTerm, Key<Site> siteKey, int limit, int offset) {
        List<Stream> rv = new ArrayList<Stream>();
        int hits = 0;
        // int max = Config.getMaxSearchResults();
        for (Stream stream : Stream.getAllStreams(siteKey)) {
            if (hits > limit) {
                break;
            }
            boolean match = false;
            // Check the stream name
            if (matchString(queryTerm, stream.getName()))
                match = true;
                // Check the owner's name
            else if (matchString(queryTerm, stream.getOwnerName()))
                match = true;
                // Now check all the tags.
            else if (stream.getTags() != null) {
                for (String tag : stream.getTags()) {
                    if (matchString(queryTerm, tag)) {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) {
                // Now check the media comments
                List<Media> mediaList = stream.getMedia(0, 0);
                Long displayLimit = new Long(Config.defaultViewLimit);
                long i = -1;
                if (mediaList != null) {
                    for (Media media : mediaList) {
                        i++;
                        if (matchString(queryTerm, media.getComments())) {
                            match = true;
                            stream.setDisplayLimit(displayLimit);
                            stream.setDisplayOffset(new Long(i));
                            break;
                        }
                    }
                }
            }

            if (match && hits >= offset) {
                rv.add(stream);
                hits++;
            }

        }
        return rv;
    }

    public static boolean matchString(String queryTerm, String test) {
/*
        for (String term : queryTerm.split("\\s")) {
            Pattern pattern = Pattern.compile(term, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(test);
            if (matcher.find())
                return true;
        }
        return false;
*/
        Pattern pattern = Pattern.compile(queryTerm, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(test);
        if (matcher.find())
            return true;
        return false;
    }
}